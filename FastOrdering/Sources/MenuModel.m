//
//  MenuModel.m
//  FastOrdering
//
//  Created by Sunder on 28/06/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "MenuModel.h"
#import "NSManagedObject+create.h"
#import "AppDelegate.h"

@implementation MenuModel

- (NSArray *)menus {
	NSFetchRequest * request = [NSFetchRequest fetchRequestWithEntityName:@"Menu"];
	NSArray * results;
	NSError * error;
	
	request.predicate = [NSPredicate predicateWithFormat:@"name != %@", kMenuALaCarteName];
	results = [self.context executeFetchRequest:request error:&error];
	if (error)
		PPLog(@"%@", error);
	return results;
}

- (Menu *)alacarte {
	NSFetchRequest * request = [NSFetchRequest fetchRequestWithEntityName:@"Menu"];
	NSArray * results;
	NSError * error;

	request.predicate = [NSPredicate predicateWithFormat:@"name = %@", kMenuALaCarteName];
	results = [self.context executeFetchRequest:request error:&error];
	if (error)
		PPLog(@"%@", error);
	return results.count > 0 ? results.firstObject : nil;
}

- (NSManagedObjectContext *)context {
	if (_context) return _context;
	return ((AppDelegate *)UIApplication.sharedApplication.delegate).managedObjectContext;
}

@end
