//
//  SocketHelper.h
//  FastOrdering
//
//  Created by Sunder on 26/01/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SocketIO+SailsIO.h"

@interface SocketHelper : NSObject <SocketIODelegate>

+ (instancetype)sharedHelper;
+ (SocketIO *)sharedSocket;
+ (void)connectSocket;

@property (nonatomic, retain) SocketIO *    socket;

@end
