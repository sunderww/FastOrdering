//
//  SocketHelper.m
//  FastOrdering
//
//  Created by Sunder on 26/01/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "SocketHelper.h"
#import "AppDelegate.h"
#import "SocketIOTransport.h"

#define kReconnectInterval	10.0

static SocketHelper *   _sharedHelper = nil;

@implementation SocketHelper

- (instancetype)init {
	if (self = [super init]) {
		delegates = [NSMutableArray new];
		eventListeners = [NSMutableDictionary new];
		forceDisconnect = NO;
	}
	return self;
}

+ (instancetype)sharedHelper {
	if (!_sharedHelper) {
		_sharedHelper = [SocketHelper new];
	}
	return _sharedHelper;
}

+ (SocketIO *)sharedSocket {
	return [SocketHelper sharedHelper].socket;
}

+ (void)connectSocket {
	SocketHelper * helper = [SocketHelper sharedHelper];
	
	if (helper.socket.isConnected) return ;
	if (!helper.socket) {
		helper.socket = [[SocketIO alloc] initWithDelegate:helper];
		helper.socket.version = V10x;
	}
	[helper.socket connectToHost:kSocketIOHost onPort:kSocketIOPort withParams:@{}];
}

+ (void)disconnect {
	[[SocketHelper sharedHelper] disconnect];
}

#pragma mark - Useful methods

- (void)disconnect {
	DLog(@"Disconnecting socket");
	[timer invalidate];
	timer = nil;
	forceDisconnect = YES;
	[self.socket sendEvent:@"logout" withData:nil];
	[self.socket disconnect];
	self.socket = nil;
	forceDisconnect = NO;
}

- (void)tryReconnect:(NSTimer *)aTimer {
	PPLog(@"Trying to reconnect socket...");
	forceDisconnect = YES;
	[self.socket disconnect];
	[self.socket connectToHost:kSocketIOHost onPort:kSocketIOPort withParams:@{}];
	[self.socket sendEvent:@"authentication" withData:@{@"user_key": userKey} andAcknowledge:^(id argsData) {
		DPPLog(@"%@", argsData);
	}];
}

- (void)authenticateWithKey:(NSString *)key andAcknowledgement:(void(^)(id argsData))ack {
	userKey = key;
	[self.socket sendEvent:@"authentication" withData:@{@"user_key": userKey} andAcknowledge:ack];
}

#pragma mark - Delegate and listener methods

- (void)pushDelegate:(id<SocketIODelegate>)delegate {
	[delegates addObject:delegate];
}

- (void)popDelegate:(id<SocketIODelegate>)delegate {
	[delegates removeObject:delegate];
}

- (void)registerListener:(id<SocketEventListener>)listener forEvent:(NSString *)eventName {
	if (!eventListeners[eventName])
		eventListeners[eventName] = [NSMutableArray new];
	
	[eventListeners[eventName] addObject:listener];
}

- (void)unregisterListener:(id<SocketEventListener>)listener forEvent:(NSString *)eventName {
	[eventListeners[eventName] removeObject:listener];
}

#pragma mark - SocketIO delegate methods

- (void)socketIO:(SocketIO *)socket didReceiveMessage:(SocketIOPacket *)packet {
	for (id<SocketIODelegate> delegate in delegates) {
		if ([delegate respondsToSelector:@selector(socketIO:didReceiveMessage:)]) {
			[delegate socketIO:socket didReceiveMessage:packet];
		}
	}
	PPLog(@"MESSAGE %@", packet);
}

- (void)socketIO:(SocketIO *)socket didReceiveJSON:(SocketIOPacket *)packet {
	for (id<SocketIODelegate> delegate in delegates) {
		if ([delegate respondsToSelector:@selector(socketIO:didReceiveJSON:)]) {
			[delegate socketIO:socket didReceiveJSON:packet];
		}
	}
	PPLog(@"JSON %@", packet);
}

- (void)socketIO:(SocketIO *)socket didReceiveEvent:(SocketIOPacket *)packet {
	PPLog(@"EVENT %@", packet.name);
	for (id<SocketIODelegate> delegate in delegates) {
		if ([delegate respondsToSelector:@selector(socketIO:didReceiveEvent:)]) {
			[delegate socketIO:socket didReceiveEvent:packet];
		}
	}

	for (id<SocketEventListener> listener in eventListeners[packet.name]) {
		[listener socketReceivedEvent:packet.name withPacket:packet];
	}
}

- (void)socketIO:(SocketIO *)socket didSendMessage:(SocketIOPacket *)packet {
	for (id<SocketIODelegate> delegate in delegates) {
		if ([delegate respondsToSelector:@selector(socketIO:didSendMessage:)]) {
			[delegate socketIO:socket didSendMessage:packet];
		}
	}
	PPLog(@"SENT %@", packet);
}

- (void)socketIODidConnect:(SocketIO *)socket {
	PPLog(@"CONNECT");
	[timer invalidate];
	timer = nil;
	forceDisconnect = NO;
}

- (void)socketIODidDisconnect:(SocketIO *)socket disconnectedWithError:(NSError *)error {
	if (!forceDisconnect && userKey) {
		PPLog(@"DECO %@", error);		
		timer = [NSTimer scheduledTimerWithTimeInterval:kReconnectInterval target:self selector:@selector(tryReconnect:) userInfo:nil repeats:YES];
	}
}

- (void)socketIO:(SocketIO *)socket onError:(NSError *)error {
	for (id<SocketIODelegate> delegate in delegates) {
		if ([delegate respondsToSelector:@selector(socketIO:onError:)]) {
			[delegate socketIO:socket onError:error];
		}
	}
	PPLog(@"%@", error);
}

@end
