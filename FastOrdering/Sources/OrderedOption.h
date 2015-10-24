//
//  OrderedOption.h
//  FastOrdering
//
//  Created by Sunder on 22/10/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Option, OrderedDish;

@interface OrderedOption : NSManagedObject

@property (nonatomic, retain) NSDate * createdAt;
@property (nonatomic, retain) NSNumber * qty;
@property (nonatomic, retain) NSString * serverId;
@property (nonatomic, retain) NSDate * updatedAt;
@property (nonatomic, retain) OrderedDish *dish;
@property (nonatomic, retain) Option *option;

@end
