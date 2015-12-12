//
//  NSManagedObject+create.h
//  Conseils de Pro
//
//  Created by Sunder on 18/04/14.
//  Copyright (c) 2014 softmobiles. All rights reserved.
//

#import <CoreData/CoreData.h>

@interface NSManagedObject (create)

+ (id)create;
+ (id)createInContext:(NSManagedObjectContext *)context;
+ (id)createWithClass:(NSString *)className;
+ (id)createWithClass:(NSString *)className inContext:(NSManagedObjectContext *)context;

+ (NSArray *)last:(NSUInteger)n withDescriptors:(NSArray *)descriptors;
+ (NSArray *)last:(NSUInteger)n skip:(NSUInteger)skip withDescriptors:(NSArray *)descriptors;

+ (NSArray *)allObjects;
+ (NSArray *)allObjectsInContext:(NSManagedObjectContext *)context;
+ (NSArray *)allObjectsByPriority;
+ (NSArray *)allObjectsSortedWithDescriptors:(NSArray *)descriptors;
+ (NSArray *)allObjectsSortedWithDescriptors:(NSArray *)descriptors inContext:(NSManagedObjectContext *)context;

+ (id)retrieveWithServerId:(NSString *)serverId inContext:(NSManagedObjectContext *)context;

- (void)setPriorityForClass:(NSString *)className;

@end
