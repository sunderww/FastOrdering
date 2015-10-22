//
//  MenuComposition.h
//  FastOrdering
//
//  Created by Sunder on 22/10/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class DishCategory, Menu;

@interface MenuComposition : NSManagedObject

@property (nonatomic, retain) NSDate * createdAt;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSNumber * price;
@property (nonatomic, retain) NSString * serverId;
@property (nonatomic, retain) NSDate * updatedAt;
@property (nonatomic, retain) NSSet *categories;
@property (nonatomic, retain) Menu *menu;
@end

@interface MenuComposition (CoreDataGeneratedAccessors)

- (void)addCategoriesObject:(DishCategory *)value;
- (void)removeCategoriesObject:(DishCategory *)value;
- (void)addCategories:(NSSet *)values;
- (void)removeCategories:(NSSet *)values;

@end
