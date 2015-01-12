//
//  WSHelper.m
//  FastOrdering
//
//  Created by Sunder on 03/01/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "WSHelper.h"
#import "AppDelegate.h"

static WSHelper *   _sharedHelper = nil;

@implementation WSHelper

+ (instancetype)sharedHelper {
    if (!_sharedHelper)
        _sharedHelper = [WSHelper new];
    return _sharedHelper;
}

- (instancetype)init {
    if ((self = [super init])) {
        queue = [NSOperationQueue new];
    }
    
    return self;
}

- (BOOL)connectWithLogin:(NSString *)login andPassword:(NSString *)password {
    return YES;
}

- (NSData *)requestWithVerb:(NSString *)verb andParams:(NSDictionary *)params atPath:(NSString *)path {
    NSString * URLString = [NSString stringWithFormat:@"http://%@:%d/%@", kSocketIOHost, kSocketIOPort, path];
    NSMutableURLRequest * request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:URLString] cachePolicy:NSURLRequestReloadIgnoringLocalAndRemoteCacheData timeoutInterval:10];
    
    [request setHTTPMethod:verb];
    DLog(@"DON'T FORGET TO ADD PARAMS");
    
    NSError * requestError;
    NSURLResponse * urlResponse = nil;
    NSData * response = [NSURLConnection sendSynchronousRequest:request returningResponse:&urlResponse error:&requestError];
    DPPLog(@">>> WS SYNCHRONOUS CALL\n%@\n%@\nERROR ? %@", urlResponse, response, requestError);
    
    return response;
}

- (NSData *)getRequestAtPath:(NSString *)path {
    return [self requestWithVerb:@"GET" andParams:@{} atPath:path];
}

- (void)asynchronousRequestWithVerb:(NSString *)verb andParams:(NSDictionary *)params atPath:(NSString *)path completion:(void (^)(NSURLResponse *, NSData *, NSError *))block {
    NSString * URLString = [NSString stringWithFormat:@"http://%@:%d/%@", kSocketIOHost, kSocketIOPort, path];
    NSMutableURLRequest * request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:URLString] cachePolicy:NSURLRequestReloadIgnoringLocalAndRemoteCacheData timeoutInterval:10];
    
    [request setHTTPMethod:verb];
    DLog(@"SHOULD CHECK HOW TO SEND ASYNC REQUEST");
    [NSURLConnection sendAsynchronousRequest:request queue:queue completionHandler:block];
}

- (void)asynchronousGetRequestAtPath:(NSString *)path completion:(void (^)(NSURLResponse * response, NSData * data, NSError * error))block {
    return [self asynchronousRequestWithVerb:@"GET" andParams:@{} atPath:path completion:block];
}

@end
