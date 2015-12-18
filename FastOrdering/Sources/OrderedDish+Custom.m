//
//  OrderedDish+Custom.m
//  FastOrdering
//
//  Created by Sunder on 08/10/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "OrderedDish+Custom.h"
#import "AppDelegate.h"
#import "OrderedOption.h"

@implementation OrderedDish (Custom)

- (void)sanitizeInContext:(NSManagedObjectContext *)context {
	for (OrderedOption * option in self.options.allObjects) {
		if (option.qty.integerValue == 0) {
			option.orderedDish = nil;
			option.option = nil;
			[context deleteObject:option];
		}
	}
}

- (void)sanitize {
	return [self sanitizeInContext:((AppDelegate *)UIApplication.sharedApplication.delegate).managedObjectContext];
}

- (OrderedOption *)orderedOptionWithOption:(Option *)option {
	for (OrderedOption * ordered in self.options.allObjects) {
		if ([ordered.option.serverId isEqualToString:option.serverId])
			return ordered;
	}
	return nil;
}

@end
