//
//  Dish.h
//  FastOrdering
//
//  Created by Sunder on 10/10/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class DishCategory, OptionCategory, OrderedDish;

@interface Dish : NSManagedObject

@property (nonatomic, retain) NSNumber * available;
@property (nonatomic, retain) NSDate * createdAt;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSNumber * price;
@property (nonatomic, retain) NSString * serverId;
@property (nonatomic, retain) NSDate * updatedAt;
@property (nonatomic, retain) NSSet *categories;
@property (nonatomic, retain) NSSet *optioncategories;
@property (nonatomic, retain) NSSet *ordered;
@end

@interface Dish (CoreDataGeneratedAccessors)

- (void)addCategoriesObject:(DishCategory *)value;
- (void)removeCategoriesObject:(DishCategory *)value;
- (void)addCategories:(NSSet *)values;
- (void)removeCategories:(NSSet *)values;

- (void)addOptioncategoriesObject:(OptionCategory *)value;
- (void)removeOptioncategoriesObject:(OptionCategory *)value;
- (void)addOptioncategories:(NSSet *)values;
- (void)removeOptioncategories:(NSSet *)values;

- (void)addOrderedObject:(OrderedDish *)value;
- (void)removeOrderedObject:(OrderedDish *)value;
- (void)addOrdered:(NSSet *)values;
- (void)removeOrdered:(NSSet *)values;

@end
