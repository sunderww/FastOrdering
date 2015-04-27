//
//  NSManagedObject+create.m
//  Conseils de Pro
//
//  Created by Sunder on 18/04/14.
//  Copyright (c) 2014 softmobiles. All rights reserved.
//

#import "NSManagedObject+create.h"
#import "AppDelegate.h"

@implementation NSManagedObject (create)

+ (NSArray *)allObjectsSortedWithDescriptors:(NSArray *)descriptors {
    NSManagedObjectContext * context = ((AppDelegate *)UIApplication.sharedApplication.delegate).managedObjectContext;
    NSFetchRequest * request = [NSFetchRequest fetchRequestWithEntityName:NSStringFromClass([self class])];
    NSArray * results;
    NSError * error;

    [request setSortDescriptors:descriptors];
    results = [context executeFetchRequest:request error:&error];
    if (error)
        PPLog(@"%@", error);
    return results;
}

+ (NSArray *)last:(NSUInteger)n skip:(NSUInteger)skip withDescriptors:(NSArray *)descriptors {
  NSManagedObjectContext * context = ((AppDelegate *)UIApplication.sharedApplication.delegate).managedObjectContext;
  NSFetchRequest * request = [NSFetchRequest fetchRequestWithEntityName:NSStringFromClass([self class])];
  NSArray * results;
  NSError * error;
  
  request.sortDescriptors = descriptors;
  request.fetchLimit = n;
  request.fetchOffset = skip;
  results = [context executeFetchRequest:request error:&error];
  if (error)
    PPLog(@"%@", error);
  return results;
}

+ (NSArray *)last:(NSUInteger)n withDescriptors:(NSArray *)descriptors {
  return [self last:n skip:0 withDescriptors:descriptors];
}

+ (NSArray *)allObjects {
    return [self allObjectsSortedWithDescriptors:@[]];
}

+ (NSArray *)allObjectsByPriority {
    return [self allObjectsSortedWithDescriptors:@[[NSSortDescriptor sortDescriptorWithKey:@"priority" ascending:YES]]];
}

+ (id)createWithClass:(NSString *)className {
    NSManagedObjectContext * context = ((AppDelegate *)UIApplication.sharedApplication.delegate).managedObjectContext;
    
    return [NSEntityDescription insertNewObjectForEntityForName:className inManagedObjectContext:context];
}

+ (id)create {
    return [self createWithClass:NSStringFromClass([self class])];
}

- (void)setPriorityForClass:(NSString *)className {
    NSManagedObjectContext * context = ((AppDelegate *)UIApplication.sharedApplication.delegate).managedObjectContext;
    NSFetchRequest * request = [NSFetchRequest fetchRequestWithEntityName:className];
    NSArray * results;
    NSError * error;
    
    [request setSortDescriptors:@[[NSSortDescriptor sortDescriptorWithKey:@"priority" ascending:NO]]];
    request.fetchLimit = 1;
    results = [context executeFetchRequest:request error:&error];
    if (error)
        PPLog(@"%@", error);

    NSNumber * priority = [results.firstObject valueForKey:@"priority"];
    [self setValue:@(priority.integerValue + 1) forKey:@"priority"];
}

@end
