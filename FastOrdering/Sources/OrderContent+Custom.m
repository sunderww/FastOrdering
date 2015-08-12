//
//  OrderContent+Custom.m
//  FastOrdering
//
//  Created by Sunder on 17/04/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "OrderContent+Custom.h"
#import "OrderedDish.h"
#import "AppDelegate.h"

@implementation OrderContent (Custom)

- (BOOL)isEmpty {
	for (OrderedDish * dish in self.dishes) {
		if (dish.quantity.integerValue > 0)
			return NO;
	}

	return YES;
}

- (void)sanitizeInContext:(NSManagedObjectContext *)context {
	for (OrderedDish * dish in [NSSet setWithSet:self.dishes]) {
		if (dish.quantity.integerValue == 0) {
			dish.order = nil;
			dish.content = nil;
			[context deleteObject:dish];
		}
	}
}

- (void)sanitize {
	return [self sanitizeInContext:((AppDelegate *)UIApplication.sharedApplication.delegate).managedObjectContext];
}

@end
