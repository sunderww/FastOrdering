//
//  OptionCategory.h
//  FastOrdering
//
//  Created by Sunder on 08/10/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Dish, Option;

@interface OptionCategory : NSManagedObject

@property (nonatomic, retain) NSString * serverId;
@property (nonatomic, retain) NSDate * createdAt;
@property (nonatomic, retain) NSDate * updatedAt;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSSet *option;
@property (nonatomic, retain) NSSet *dishes;
@end

@interface OptionCategory (CoreDataGeneratedAccessors)

- (void)addOptionObject:(Option *)value;
- (void)removeOptionObject:(Option *)value;
- (void)addOption:(NSSet *)values;
- (void)removeOption:(NSSet *)values;

- (void)addDishesObject:(Dish *)value;
- (void)removeDishesObject:(Dish *)value;
- (void)addDishes:(NSSet *)values;
- (void)removeDishes:(NSSet *)values;

@end
