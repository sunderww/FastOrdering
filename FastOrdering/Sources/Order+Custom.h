//
//  Order+Custom.h
//  FastOrdering
//
//  Created by Sunder on 06/04/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "Order.h"
#import "Dish.h"
#import "MenuComposition.h"

@interface Order (Custom)

- (NSDictionary *)toJSON;
- (NSDictionary *)toJSONTest; // Replaces NSArrays with NSCountedSet for comparing
- (NSData *)toJSONData;
- (NSString *)toJSONString;

- (void)sanitizeInContext:(NSManagedObjectContext *)context;
- (void)sanitize;

- (NSArray *)dishesGroupedByMenu;
- (NSArray *)alacarteDishes;
- (OrderedDish *)orderedDishWithDish:(Dish *)dish andComposition:(MenuComposition *)composition;

@end
