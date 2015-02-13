//
//  Dish+Custom.m
//  FastOrdering
//
//  Created by Sunder on 05/01/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "Dish+Custom.h"
#import "DishCategory.h"
#import "AppDelegate.h"

@implementation Dish (Custom)

+ (NSArray *)dishesWithParentId:(NSString *)parentId {
    NSManagedObjectContext * context = ((AppDelegate *)UIApplication.sharedApplication.delegate).managedObjectContext;
    NSFetchRequest * request = [NSFetchRequest fetchRequestWithEntityName:NSStringFromClass([self class])];
    NSArray * results;
    NSError * error;
    
    if (!parentId) {
        request.predicate = [NSPredicate predicateWithFormat:@"categories.@count = 0"];
//        DLog(@"Find how to search with nil in coredata");
//        return @[];
    } else {
        request.predicate = [NSPredicate predicateWithFormat:@"ANY categories.serverId = %@" argumentArray:@[parentId]];
    }

    results = [context executeFetchRequest:request error:&error];
    if (error)
        PPLog(@"%@", error);
    return results;
}

+ (NSArray *)dishesWithParent:(DishCategory *)parent {
    return [self dishesWithParentId:parent.serverId];
}

@end
