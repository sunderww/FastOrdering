//
//  OrderedDish+Custom.h
//  FastOrdering
//
//  Created by Sunder on 08/10/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "OrderedDish.h"
#import "Option.h"

@interface OrderedDish (Custom)

- (void)sanitize;
- (void)sanitizeInContext:(NSManagedObjectContext *)context;
- (OrderedOption *)orderedOptionWithOption:(Option *)option;

@end
