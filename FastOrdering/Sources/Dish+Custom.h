//
//  Dish+Custom.h
//  FastOrdering
//
//  Created by Sunder on 05/01/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "Dish.h"

@interface Dish (Custom)

+ (NSArray *)dishesWithParentId:(NSString *)parentId;
+ (NSArray *)dishesWithParent:(DishCategory *)parent;

@end
