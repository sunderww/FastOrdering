//
//  OrderContent.h
//  FastOrdering
//
//  Created by Sunder on 06/04/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class MenuComposition, Order, OrderedDish;

@interface OrderContent : NSManagedObject

@property (nonatomic, retain) NSDate * createdAt;
@property (nonatomic, retain) NSDate * updatedAt;
@property (nonatomic, retain) NSString * serverId;
@property (nonatomic, retain) NSNumber * quantity;
@property (nonatomic, retain) NSString * comment;
@property (nonatomic, retain) NSSet *dishes;
@property (nonatomic, retain) MenuComposition *menuComposition;
@property (nonatomic, retain) Order *order;
@end

@interface OrderContent (CoreDataGeneratedAccessors)

- (void)addDishesObject:(OrderedDish *)value;
- (void)removeDishesObject:(OrderedDish *)value;
- (void)addDishes:(NSSet *)values;
- (void)removeDishes:(NSSet *)values;

@end
