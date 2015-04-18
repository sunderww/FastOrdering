//
//  DishCategory+Custom.h
//  FastOrdering
//
//  Created by Sunder on 05/01/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "DishCategory.h"

@interface DishCategory (Custom)

+ (NSArray *)categoriesWithParentId:(NSString *)parentId;
+ (NSArray *)categoriesWithParent:(DishCategory *)parent;

- (NSArray *)availableDishes;

@end
