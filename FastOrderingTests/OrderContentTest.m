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

- (void)testSanitize {
	OrderContent * content = [OrderContent createInContext:self.moc];
	NSMutableArray * dishes = [NSMutableArray new];
	NSMutableArray * emptyDishes = [NSMutableArray new];
	Dish * dish;
	OrderedDish * ordered;

	XCTAssertEqual(content.dishes.count, 0, @"OrderContent should not have dishes");
	[content sanitizeInContext:self.moc];
	XCTAssertEqual(content.dishes.count, 0, @"OrderContent should not have dishes");
	
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
	
	content = [OrderContent createInContext:self.moc];
	dish = [Dish createInContext:self.moc];
	dish.name = @"Dish 4";
	ordered = [OrderedDish createInContext:self.moc];
	ordered.dish = dish;
	ordered.content = content;
	ordered.quantity = @0;
	[dishes addObject:ordered];
	[content sanitizeInContext:self.moc];
	XCTAssertEqual(content.dishes.count, 0, @"OrderContent should not have dishes");
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
