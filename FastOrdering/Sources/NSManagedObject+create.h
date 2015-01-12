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
+ (id)createWithClass:(NSString *)className;

+ (NSArray *)allObjects;
+ (NSArray *)allObjectsByPriority;
+ (NSArray *)allObjectsSortedWithDescriptors:(NSArray *)descriptors;

- (void)setPriorityForClass:(NSString *)className;

@end
