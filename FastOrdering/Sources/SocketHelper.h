//
//  SocketHelper.h
//  FastOrdering
//
//  Created by Sunder on 26/01/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SocketIO+SailsIO.h"

@class AppDelegate;

@protocol SocketEventListener <NSObject>

@required
- (void)socketReceivedEvent:(NSString *)name withPacket:(SocketIOPacket *)packet;

@end

@interface SocketHelper : NSObject <SocketIODelegate> {
	NSMutableArray *		delegates;
	NSMutableDictionary *	eventListeners;
	AppDelegate *			appDelegate;
	NSTimer *				timer;
	BOOL					forceDisconnect;
	NSString *				userKey;
}

+ (instancetype)sharedHelper;
+ (SocketIO *)sharedSocket;
+ (void)connectSocket;
+ (void)disconnect;

@property (nonatomic, retain) SocketIO *    socket;

- (void)pushDelegate:(id<SocketIODelegate>)delegate;
- (void)popDelegate:(id<SocketIODelegate>)delegate;
- (void)registerListener:(id<SocketEventListener>)listener forEvent:(NSString *)eventName;
- (void)unregisterListener:(id<SocketEventListener>)listener forEvent:(NSString *)eventName;

- (void)authenticateWithKey:(NSString *)key andAcknowledgement:(void(^)(id argsData))ack;

@end
