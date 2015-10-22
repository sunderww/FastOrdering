//
//  OrderedDish.h
//  FastOrdering
//
//  Created by Sunder on 22/10/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Dish, Menu, Order, OrderedOption;

@interface OrderedDish : NSManagedObject

@property (nonatomic, retain) NSString * comment;
@property (nonatomic, retain) NSDate * createdAt;
@property (nonatomic, retain) NSNumber * quantity;
@property (nonatomic, retain) NSString * serverId;
@property (nonatomic, retain) NSNumber * status;
@property (nonatomic, retain) NSDate * updatedAt;
@property (nonatomic, retain) Dish *dish;
@property (nonatomic, retain) Menu *menu;
@property (nonatomic, retain) NSSet *options;
@property (nonatomic, retain) Order *order;
@end

@interface OrderedDish (CoreDataGeneratedAccessors)

- (void)addOptionsObject:(OrderedOption *)value;
- (void)removeOptionsObject:(OrderedOption *)value;
- (void)addOptions:(NSSet *)values;
- (void)removeOptions:(NSSet *)values;

@end
