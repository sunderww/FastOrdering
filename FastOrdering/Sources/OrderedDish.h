//
//  OrderedDish.h
//  FastOrdering
//
//  Created by Sunder on 06/10/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Dish, Order, OrderContent, OrderedOption;

@interface OrderedDish : NSManagedObject

@property (nonatomic, retain) NSString * comment;
@property (nonatomic, retain) NSDate * createdAt;
@property (nonatomic, retain) NSNumber * quantity;
@property (nonatomic, retain) NSString * serverId;
@property (nonatomic, retain) NSNumber * status;
@property (nonatomic, retain) NSDate * updatedAt;
@property (nonatomic, retain) OrderContent *content;
@property (nonatomic, retain) Dish *dish;
@property (nonatomic, retain) Order *order;
@property (nonatomic, retain) NSSet *options;
@end

@interface OrderedDish (CoreDataGeneratedAccessors)

- (void)addOptionsObject:(OrderedOption *)value;
- (void)removeOptionsObject:(OrderedOption *)value;
- (void)addOptions:(NSSet *)values;
- (void)removeOptions:(NSSet *)values;

@end
