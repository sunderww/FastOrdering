//
//  OrderContentTest.m
//  FastOrdering
//
//  Created by Sunder on 29/06/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

@import CoreData;

#import <UIKit/UIKit.h>
#import <XCTest/XCTest.h>
#import "NSManagedObject+create.h"
#import "OrderContent+Custom.h"
#import "Dish.h"
#import "OrderedDish.h"

@interface OrderContentTest : XCTestCase

@property (nonatomic, retain) NSManagedObjectContext * moc;

@end

@implementation OrderContentTest

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

- (void)testSanitizeEmpty {
	OrderContent * content = [OrderContent createInContext:self.moc];
	XCTAssertEqual(content.dishes.count, 0, @"OrderContent should not have dishes");
	[content sanitizeInContext:self.moc]; // Test essentially that it doesn't crash
	XCTAssertEqual(content.dishes.count, 0, @"OrderContent should not have dishes");
}

- (void)testSanitizeWithSanitizing {
	OrderContent * content = [OrderContent createInContext:self.moc];

	Dish * dish = [Dish createInContext:self.moc];
	dish.name = @"Empty dish";

	OrderedDish * ordered = [OrderedDish createInContext:self.moc];
	ordered.dish = dish;
	ordered.content = content;
	ordered.quantity = @0;

	XCTAssertEqual(content.dishes.count, 1, @"OrderContent should have 1 dish");
	[content sanitizeInContext:self.moc];
	XCTAssertEqual(content.dishes.count, 0, @"OrderContent should not have dishes");
}

- (void)testSanitizeWithEmptyQuantity {
	OrderContent * content = [OrderContent createInContext:self.moc];
	
	Dish * dish = [Dish createInContext:self.moc];
	dish.name = @"No Quantity Dish";
	
	OrderedDish * ordered = [OrderedDish createInContext:self.moc];
	ordered.dish = dish;
	ordered.content = content;

	XCTAssertEqual(content.dishes.count, 1, @"OrderContent should have 1 dish");
	[content sanitizeInContext:self.moc];
	XCTAssertEqual(content.dishes.count, 0, @"OrderContent should not have dishes");
}

- (void)testSanitizeWithoutSanitizing {
	OrderContent * content = [OrderContent createInContext:self.moc];
	NSMutableArray * dishes = [NSMutableArray new];
	Dish * dish;
	OrderedDish * ordered;
	
	// First dish
	dish = [Dish createInContext:self.moc];
	dish.name = @"Dish 1";
	ordered = [OrderedDish createInContext:self.moc];
	ordered.dish = dish;
	ordered.content = content;
	ordered.quantity = @2;
	[dishes addObject:ordered];
	
	// Second dish
	dish = [Dish createInContext:self.moc];
	dish.name = @"Dish 2";
	ordered = [OrderedDish createInContext:self.moc];
	ordered.dish = dish;
	ordered.content = content;
	ordered.quantity = @3;
	[dishes addObject:ordered];
	
	// Third dish
	dish = [Dish createInContext:self.moc];
	dish.name = @"Dish 3";
	ordered = [OrderedDish createInContext:self.moc];
	ordered.dish = dish;
	ordered.content = content;
	ordered.quantity = @1;
	[dishes addObject:ordered];
	
	XCTAssertEqual(content.dishes.count, dishes.count, @"OrderContent should have %lu dishes", dishes.count);
	[content sanitizeInContext:self.moc];
	XCTAssertEqual(content.dishes.count, dishes.count, @"OrderContent should have %lu dishes", dishes.count);
}

- (void)testSanitizeComplete {
	OrderContent * content = [OrderContent createInContext:self.moc];
	NSMutableArray * dishes = [NSMutableArray new];
	NSMutableArray * emptyDishes = [NSMutableArray new];
	Dish * dish;
	OrderedDish * ordered;
	
	// First dish
	dish = [Dish createInContext:self.moc];
	dish.name = @"Dish 1";
	ordered = [OrderedDish createInContext:self.moc];
	ordered.dish = dish;
	ordered.content = content;
	ordered.quantity = @0;
	[emptyDishes addObject:ordered];
	[dishes addObject:ordered];
	
	// Second dish
	dish = [Dish createInContext:self.moc];
	dish.name = @"Dish 2";
	ordered = [OrderedDish createInContext:self.moc];
	ordered.dish = dish;
	ordered.content = content;
	ordered.quantity = @0;
	[emptyDishes addObject:ordered];
	[dishes addObject:ordered];
	
	// Third dish
	dish = [Dish createInContext:self.moc];
	dish.name = @"Dish 3 (not empty)";
	ordered = [OrderedDish createInContext:self.moc];
	ordered.dish = dish;
	ordered.content = content;
	ordered.quantity = @1;
	[dishes addObject:ordered];
	
	XCTAssertEqual(content.dishes.count, dishes.count, @"OrderContent should have %lu dishes", dishes.count);
	[content sanitizeInContext:self.moc];
	XCTAssertEqual(content.dishes.count, dishes.count - emptyDishes.count, @"OrderContent should have %lu dishes", dishes.count - emptyDishes.count);
	[content sanitizeInContext:self.moc];
	XCTAssertEqual(content.dishes.count, dishes.count - emptyDishes.count, @"OrderContent should have %lu dishes", dishes.count - emptyDishes.count);
}

- (void)testIsEmpty {
	OrderContent * content = [OrderContent createInContext:self.moc];
	Dish * dish;
	OrderedDish * ordered;
	
	XCTAssert(content.isEmpty, @"Content should be empty");

	// First dish
	dish = [Dish createInContext:self.moc];
	dish.name = @"Dish 1";
	ordered = [OrderedDish createInContext:self.moc];
	ordered.dish = dish;
	ordered.content = content;
	ordered.quantity = @0;
	XCTAssert(content.isEmpty, @"Content should be empty");
	
	// Second dish
	dish = [Dish createInContext:self.moc];
	dish.name = @"Dish 2";
	ordered = [OrderedDish createInContext:self.moc];
	ordered.dish = dish;
	ordered.content = content;
	ordered.quantity = @0;
	XCTAssert(content.isEmpty, @"Content should be empty");
	
	// Third dish
	dish = [Dish createInContext:self.moc];
	dish.name = @"Dish 3 (not empty)";
	ordered = [OrderedDish createInContext:self.moc];
	ordered.dish = dish;
	ordered.content = content;
	ordered.quantity = @1;
	XCTAssert(!content.isEmpty, @"Content should not be empty");
}


@end
