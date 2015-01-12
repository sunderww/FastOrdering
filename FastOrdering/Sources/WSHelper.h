//
//  WSHelper.h
//  FastOrdering
//
//  Created by Sunder on 03/01/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface WSHelper : NSObject {
    NSOperationQueue *  queue;
}

+ (instancetype)sharedHelper;

- (BOOL)connectWithLogin:(NSString *)login andPassword:(NSString *)password;

- (NSData *)requestWithVerb:(NSString *)verb andParams:(NSDictionary *)params atPath:(NSString *)path;
- (NSData *)getRequestAtPath:(NSString *)path;

- (void)asynchronousRequestWithVerb:(NSString *)verb andParams:(NSDictionary *)params atPath:(NSString *)path completion:(void(^)(NSURLResponse * response, NSData * data, NSError * error))block;
- (void)asynchronousGetRequestAtPath:(NSString *)path completion:(void(^)(NSURLResponse * response, NSData * data, NSError * error))block;

@end
