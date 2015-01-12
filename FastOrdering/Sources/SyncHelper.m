//
//  SyncHelper.m
//  FastOrdering
//
//  Created by Sunder on 02/01/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <CoreData/CoreData.h>
#import "SyncHelper.h"
#import "AppDelegate.h"
#import "WSHelper.h"

@implementation SyncHelper

- (void)syncClassNamed:(NSString *)name synchronous:(BOOL)synchronous {
    if ([self.delegate respondsToSelector:@selector(syncDidStartForClass:)])
        [self.delegate syncDidStartForClass:name];
    
    DLog(@"ADD CORRECT PARAMS");
    if (synchronous) {
        NSData * data = [WSHelper.sharedHelper requestWithVerb:@"GET" andParams:@{} atPath:name.lowercaseString];
        [self syncClass:name objectsWithData:data];
    } else {
        [WSHelper.sharedHelper asynchronousRequestWithVerb:@"GET" andParams:@{} atPath:name.lowercaseString completion:^(NSURLResponse *response, NSData *data, NSError *error) {
            if (error) {
                PPLog(@"%@", error);
            } else {
                [self syncClass:name objectsWithData:data];
            }
        }];
    }
}

- (void)syncClassNamed:(NSString *)name {
    [self syncClassNamed:name synchronous:NO];
}

- (void)syncDeletedObjectsOfClasses:(NSArray *)classes {
    [self syncDeletedObjectsOfClasses:classes synchronous:NO];
}

- (void)syncDeletedObjectsOfClasses:(NSArray *)classes synchronous:(BOOL)synchronous {
    DLog(@"SHOULD START DELETE SYNC");
}

#pragma mark - Helper methods

- (void)syncClass:(NSString *)name objectsWithData:(NSData *)data {
    if ([self.delegate respondsToSelector:@selector(syncDidStopForClass:)])
        [self.delegate syncDidStopForClass:name];
}

- (NSDate *)latestUpdateOfClass:(NSString *)class {
    NSSortDescriptor * sortDescriptor = [NSSortDescriptor sortDescriptorWithKey:@"updatedAt" ascending:NO];
    NSFetchRequest * request = [NSFetchRequest fetchRequestWithEntityName:class];
    NSArray * results;
    NSDate * latestUpdate;
    NSError * error;
    
    [request setSortDescriptors:[NSArray arrayWithObject:sortDescriptor]];
    [request setFetchLimit:1];
    
    results = [self.managedObjectContext executeFetchRequest:request error:&error];
    if (error) {
        if ([self.delegate respondsToSelector:@selector(syncDidFailWithError:forClass:)])
            [self.delegate syncDidFailWithError:error forClass:class];
        return nil;
    }
    
    if (results.count) {
        latestUpdate = [results.firstObject valueForKey:@"updatedAt"];
    } else
        latestUpdate = [NSDate dateWithTimeIntervalSince1970:0];
    return latestUpdate;
}

- (NSManagedObjectContext *)managedObjectContext {
    AppDelegate * appDelegate = (AppDelegate *)UIApplication.sharedApplication.delegate;
    return appDelegate.managedObjectContext;
}


@end
