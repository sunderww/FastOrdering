//
//  DishCategory.h
//  FastOrdering
//
//  Created by Sunder on 12/11/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Dish, DishCategory, MenuComposition;

@interface DishCategory : NSManagedObject

@property (nonatomic, retain) NSString * colorString;
@property (nonatomic, retain) NSDate * createdAt;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSNumber * price;
@property (nonatomic, retain) NSString * serverId;
@property (nonatomic, retain) NSDate * updatedAt;
@property (nonatomic, retain) NSSet *compositions;
@property (nonatomic, retain) NSSet *dishes;
@property (nonatomic, retain) DishCategory *parent;
@property (nonatomic, retain) NSSet *subcategories;
@end

@interface DishCategory (CoreDataGeneratedAccessors)

- (void)addCompositionsObject:(MenuComposition *)value;
- (void)removeCompositionsObject:(MenuComposition *)value;
- (void)addCompositions:(NSSet *)values;
- (void)removeCompositions:(NSSet *)values;

- (void)addDishesObject:(Dish *)value;
- (void)removeDishesObject:(Dish *)value;
- (void)addDishes:(NSSet *)values;
- (void)removeDishes:(NSSet *)values;

- (void)addSubcategoriesObject:(DishCategory *)value;
- (void)removeSubcategoriesObject:(DishCategory *)value;
- (void)addSubcategories:(NSSet *)values;
- (void)removeSubcategories:(NSSet *)values;

@end
