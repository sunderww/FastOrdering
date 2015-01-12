//
//  DishCategory.h
//  FastOrdering
//
//  Created by Sunder on 02/01/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Dish, DishCategory;

@interface DishCategory : NSManagedObject

@property (nonatomic, retain) NSString * colorString;
@property (nonatomic, retain) NSDate * createdAt;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSString * serverId;
@property (nonatomic, retain) NSDate * updatedAt;
@property (nonatomic, retain) NSNumber * price;
@property (nonatomic, retain) NSSet *subcategories;
@property (nonatomic, retain) NSSet *dishes;
@property (nonatomic, retain) DishCategory *parent;
@end

@interface DishCategory (CoreDataGeneratedAccessors)

- (void)addSubcategoriesObject:(DishCategory *)value;
- (void)removeSubcategoriesObject:(DishCategory *)value;
- (void)addSubcategories:(NSSet *)values;
- (void)removeSubcategories:(NSSet *)values;

- (void)addDishesObject:(Dish *)value;
- (void)removeDishesObject:(Dish *)value;
- (void)addDishes:(NSSet *)values;
- (void)removeDishes:(NSSet *)values;

@end
