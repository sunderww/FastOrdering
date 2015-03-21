////
////  Menu+Custom.m
////  FastOrdering
////
////  Created by Sunder on 20/03/2015.
////  Copyright (c) 2015 lucas.bergognon. All rights reserved.
////
//
//#import "Menu+Custom.h"
//#import "AppDelegate.h"
//
//@implementation Menu (Custom)
//
//+ (NSArray *)menusWithParentId:(NSString *)parentId {
//  NSManagedObjectContext * context = ((AppDelegate *)UIApplication.sharedApplication.delegate).managedObjectContext;
//  NSFetchRequest * request = [NSFetchRequest fetchRequestWithEntityName:NSStringFromClass([self class])];
//  NSArray * results;
//  NSError * error;
//  
//  if (!parentId) {
//    request.predicate = [NSPredicate predicateWithFormat:@"parent = nil"];
//    //        DLog(@"Find how to search with nil in coredata");
//    //        return @[];
//  } else {
//    request.predicate = [NSPredicate predicateWithFormat:@"parent.serverId = %@" argumentArray:@[parentId]];
//  }
//  
//  results = [context executeFetchRequest:request error:&error];
//  if (error)
//    PPLog(@"%@", error);
//  return results;
//}
//
//+ (NSArray *)menusWithParent:(Menu *)parent {
//  return [self menusWithParentId:parent.serverId];
//}
//
//@end
