//
//  SocketHelper.m
//  FastOrdering
//
//  Created by Sunder on 26/01/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "SocketHelper.h"
#import "AppDelegate.h"

static SocketHelper *   _sharedHelper = nil;

@implementation SocketHelper

+ (instancetype)sharedHelper {
    if (!_sharedHelper)
        _sharedHelper = [SocketHelper new];
    return _sharedHelper;
}

+ (SocketIO *)sharedSocket {
    return [SocketHelper sharedHelper].socket;
}

+ (void)connectSocket {
    SocketHelper * helper = [SocketHelper sharedHelper];

    if (helper.socket.isConnected) return ;
    if (!helper.socket)
        helper.socket = [[SocketIO alloc] initWithDelegate:helper];
    [helper.socket connectToHost:kSocketIOHost onPort:kSocketIOPort withParams:@{}];
}

#pragma mark - SocketIO delegate methods

- (void)socketIO:(SocketIO *)socket didReceiveMessage:(SocketIOPacket *)packet {
    PPLog(@"MESSAGE %@", packet);
}

- (void)socketIO:(SocketIO *)socket didReceiveJSON:(SocketIOPacket *)packet {
    PPLog(@"JSON %@", packet);
}

- (void)socketIO:(SocketIO *)socket didReceiveEvent:(SocketIOPacket *)packet {
    PPLog(@"EVENT %@", packet);
}

- (void)socketIO:(SocketIO *)socket didSendMessage:(SocketIOPacket *)packet {
    PPLog(@"SENT %@", packet);
}

- (void)socketIODidConnect:(SocketIO *)socket {
    PPLog(@"CONNECT");
}

- (void)socketIODidDisconnect:(SocketIO *)socket disconnectedWithError:(NSError *)error {
    PPLog(@"DECO %@", error);
}

- (void)socketIO:(SocketIO *)socket onError:(NSError *)error {
    PPLog(@"%@", error);
}

@end
