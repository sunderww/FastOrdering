//
//  DishCategoryTest.m
//  FastOrdering
//
//  Created by Sunder on 28/06/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

@import CoreData;

#import <UIKit/UIKit.h>
#import <XCTest/XCTest.h>
#import "DishCategory+Custom.h"
#import "NSManagedObject+create.h"
#import "Dish.h"

@interface DishCategoryTest : XCTestCase

@property (nonatomic, retain) NSManagedObjectContext * moc;

@end

@implementation DishCategoryTest

- (void)setUp {
    [super setUp];

	NSURL *modelURL = [[NSBundle mainBundle] URLForResource:@"FastOrdering" withExtension:@"momd"];
	NSManagedObjectModel *mom = [[NSManagedObjectModel alloc] initWithContentsOfURL:modelURL];
	NSPersistentStoreCoordinator *psc = [[NSPersistentStoreCoordinator alloc] initWithManagedObjectModel:mom];
	XCTAssertTrue([psc addPersistentStoreWithType:NSInMemoryStoreType configuration:nil URL:nil options:nil error:NULL] ? YES : NO, @"Should be able to add in-memory store");
	self.moc = [[NSManagedObjectContext alloc] init];
	self.moc.persistentStoreCoordinator = psc;
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];
}

- (void)testAvailableDishes {
	DishCategory * category = [DishCategory createInContext:self.moc];
	
	XCTAssertEqual(category.availableDishes.count, 0, @"Category should not have available dishes");
	
	NSArray * available = @[@"Available 1", @"Available 2", @"Available 3", @"Available 4"];
	NSArray * notAvailable = @[@"Not available 1", @"Not available 2"];
	
	for (NSString * dishName in available) {
		Dish * dish = [Dish createInContext:self.moc];
		dish.name = dishName;
		dish.available = @YES;
		[category addDishesObject:dish];
	}

	for (NSString * dishName in notAvailable) {
		Dish * dish = [Dish createInContext:self.moc];
		dish.name = dishName;
		dish.available = @NO;
		[category addDishesObject:dish];
	}
	
	XCTAssertEqual(category.availableDishes.count, available.count, @"Category should have %lu available dishes", (unsigned long)available.count);
	for (Dish * dish in category.availableDishes) {
		XCTAssertNotEqual([available indexOfObject:dish.name], NSNotFound, @"The dish %@ should be available", dish.name);
		XCTAssertEqual([notAvailable indexOfObject:dish.name], NSNotFound, @"The dish %@ should not be available", dish.name);
	}
}

@end
