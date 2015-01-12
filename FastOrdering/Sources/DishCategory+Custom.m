//
//  DishCategory+Custom.m
//  FastOrdering
//
//  Created by Sunder on 05/01/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "DishCategory+Custom.h"
#import "AppDelegate.h"

@implementation DishCategory (Custom)

+ (NSArray *)categoriesWithParentId:(NSString *)parentId {
    NSManagedObjectContext * context = ((AppDelegate *)UIApplication.sharedApplication.delegate).managedObjectContext;
    NSFetchRequest * request = [NSFetchRequest fetchRequestWithEntityName:NSStringFromClass([self class])];
    NSArray * results;
    NSError * error;
    
    if (!parentId) {
        DLog(@"Find how to search with nil in coredata");
        return @[];
    }

    request.predicate = [NSPredicate predicateWithFormat:@"serverId = %@" argumentArray:@[parentId]];
    results = [context executeFetchRequest:request error:&error];
    if (error)
        PPLog(@"%@", error);
    return results;
}

+ (NSArray *)categoriesWithParent:(DishCategory *)parent {
    return [self categoriesWithParentId:parent.serverId];
}

@end
