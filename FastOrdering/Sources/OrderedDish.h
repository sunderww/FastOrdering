//
//  OrderedDish.h
//  FastOrdering
//
//  Created by Sunder on 18/03/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Dish, Order;

@interface OrderedDish : NSManagedObject

@property (nonatomic, retain) NSString * comment;
@property (nonatomic, retain) NSDate * createdAt;
@property (nonatomic, retain) NSString * serverId;
@property (nonatomic, retain) NSNumber * status;
@property (nonatomic, retain) NSDate * updatedAt;
@property (nonatomic, retain) Dish *dish;
@property (nonatomic, retain) Order *order;

@end
