//
//  Order.h
//  FastOrdering
//
//  Created by Sunder on 06/04/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class OrderContent, OrderedDish, Table;

@interface Order : NSManagedObject

@property (nonatomic, retain) NSDate * createdAt;
@property (nonatomic, retain) NSNumber * price;
@property (nonatomic, retain) NSString * serverId;
@property (nonatomic, retain) NSDate * updatedAt;
@property (nonatomic, retain) NSString * waiter_id;
@property (nonatomic, retain) NSNumber * dinerNumber;
@property (nonatomic, retain) NSString * comments;
@property (nonatomic, retain) NSSet *dishes;
@property (nonatomic, retain) Table *table;
@property (nonatomic, retain) NSSet *orderContents;
@end

@interface Order (CoreDataGeneratedAccessors)

- (void)addDishesObject:(OrderedDish *)value;
- (void)removeDishesObject:(OrderedDish *)value;
- (void)addDishes:(NSSet *)values;
- (void)removeDishes:(NSSet *)values;

- (void)addOrderContentsObject:(OrderContent *)value;
- (void)removeOrderContentsObject:(OrderContent *)value;
- (void)addOrderContents:(NSSet *)values;
- (void)removeOrderContents:(NSSet *)values;

@end
