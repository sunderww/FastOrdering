//
//  OrderTests.m
//  FastOrdering
//
//  Created by Sunder on 01/06/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <XCTest/XCTest.h>

#import "Order+Custom.h"
#import "OrderedDish.h"
#import "NSManagedObject+create.h"
#import "Menu+Custom.h"
#import "Menu.h"
#import "MenuComposition.h"
#import "Dish+Custom.h"
#import "DishCategory+Custom.h"
#import "MenuModel.h"

@import CoreData;

@interface OrderTests : XCTestCase

@property (nonatomic, retain) NSManagedObjectContext * moc;

@end

@implementation OrderTests

- (void)populateDB {
	Menu * menu = [Menu createInContext:self.moc];
	MenuComposition * compo = [MenuComposition createInContext:self.moc];
	Dish * dish = [Dish createInContext:self.moc];
	DishCategory * category = [DishCategory createInContext:self.moc];
	
	// Menu
	menu.createdAt = [NSDate date];
	menu.updatedAt = [NSDate date];
	menu.serverId = @"menu";
	
	// First MenuComposition
	compo.createdAt = [NSDate date];
	compo.updatedAt = [NSDate date];
	compo.serverId = @"compo1";
	compo.name = @"entree+plat";
	compo.price = @1;
	[menu addCompositionsObject:compo];
	
	// Entrée category
	category.createdAt = [NSDate date];
	category.updatedAt = [NSDate date];
	category.serverId = @"cat1";
	category.name = @"entree";
	
	dish.createdAt = [NSDate date];
	dish.updatedAt = [NSDate date];
	dish.serverId = @"dish1";
	dish.name = @"entree 1";
	dish.available = @YES;
	[category addDishesObject:dish];
	
	dish = [Dish createInContext:self.moc];
	dish.createdAt = [NSDate date];
	dish.updatedAt = [NSDate date];
	dish.serverId = @"dish2";
	dish.name = @"entree 2";
	dish.available = @YES;
	[category addDishesObject:dish];
	[compo addCategoriesObject:category];
	
	// Main category
	category = [DishCategory createInContext:self.moc];
	category.createdAt = [NSDate date];
	category.updatedAt = [NSDate date];
	category.serverId = @"cat2";
	category.name = @"plat";
	
	dish = [Dish createInContext:self.moc];
	dish.createdAt = [NSDate date];
	dish.updatedAt = [NSDate date];
	dish.serverId = @"dish3";
	dish.name = @"plat 1";
	dish.available = @YES;
	[category addDishesObject:dish];
	
	dish = [Dish createInContext:self.moc];
	dish.createdAt = [NSDate date];
	dish.updatedAt = [NSDate date];
	dish.serverId = @"dish4";
	dish.name = @"plat 2";
	dish.available = @YES;
	[category addDishesObject:dish];
	[compo addCategoriesObject:category];
	
	// Second MenuComposition
	compo = [MenuComposition createInContext:self.moc];
	compo.createdAt = [NSDate date];
	compo.updatedAt = [NSDate date];
	compo.serverId = @"compo2";
	compo.name = @"plat+dessert";
	compo.price = @2;
	[menu addCompositionsObject:compo];
	[compo addCategoriesObject:category];
	
	// Dessert category
	category = [DishCategory createInContext:self.moc];
	category.createdAt = [NSDate date];
	category.updatedAt = [NSDate date];
	category.serverId = @"cat3";
	category.name = @"dessert";
	
	dish = [Dish createInContext:self.moc];
	dish.createdAt = [NSDate date];
	dish.updatedAt = [NSDate date];
	dish.serverId = @"dish5";
	dish.name = @"dessert 1";
	dish.available = @YES;
	[category addDishesObject:dish];
	
	dish = [Dish createInContext:self.moc];
	dish.createdAt = [NSDate date];
	dish.updatedAt = [NSDate date];
	dish.serverId = @"dish6";
	dish.name = @"dessert 2";
	dish.price = @1;
	dish.available = @YES;
	[category addDishesObject:dish];
	[compo addCategoriesObject:category];
	

	// Alacarte
	menu = [Menu createInContext:self.moc];
	menu.name = kMenuALaCarteName;
	menu.serverId = @"menu a la carte";
	menu.createdAt = [NSDate date];
	menu.updatedAt = [NSDate date];
	
	// Alacarte desserts
	compo = [MenuComposition createInContext:self.moc];
	compo.createdAt = [NSDate date];
	compo.updatedAt = [NSDate date];
	compo.serverId = @"Carte_Dessert";
	compo.name = @"Desserts";
	[menu addCompositionsObject:compo];
	
	// Alacarte category desserts
	category = [DishCategory createInContext:self.moc];
	category.createdAt = [NSDate date];
	category.updatedAt = [NSDate date];
	category.serverId = @"Carte_Category_1";
	category.name = @"Desserts";
	[compo addCategoriesObject:category];
	
	// Alacarte dessert dishes - we add one dessert from a menu and a second dessert
	[category addDishesObject:dish];
	dish = [Dish createInContext:self.moc];
	dish.createdAt = [NSDate date];
	dish.updatedAt = [NSDate date];
	dish.serverId = @"dish7";
	dish.price = @3;
	dish.name = @"dessert 3";
	dish.available = @YES;
	[category addDishesObject:dish];
	
	// Alacarte main
	compo = [MenuComposition createInContext:self.moc];
	compo.createdAt = [NSDate date];
	compo.updatedAt = [NSDate date];
	compo.serverId = @"Carte_Main";
	compo.name = @"Desserts";
	[menu addCompositionsObject:compo];

	// Alacarte category main
	category = [DishCategory createInContext:self.moc];
	category.createdAt = [NSDate date];
	category.updatedAt = [NSDate date];
	category.serverId = @"Carte_Category_2";
	category.name = @"Plats";
	[compo addCategoriesObject:category];
	
	// Alacarte main dishes
	dish = [Dish createInContext:self.moc];
	dish.createdAt = [NSDate date];
	dish.updatedAt = [NSDate date];
	dish.serverId = @"dish8";
	dish.name = @"plat alacarte 1";
	dish.price = @5;
	dish.available = @YES;
	[category addDishesObject:dish];
	
	dish = [Dish createInContext:self.moc];
	dish.createdAt = [NSDate date];
	dish.updatedAt = [NSDate date];
	dish.serverId = @"dish9";
	dish.name = @"plat alacarte 2";
	dish.price = @3.4;
	dish.available = @NO;
	[category addDishesObject:dish];
	
	[self.moc save:NULL];
}

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
	self.moc = nil;
	[super tearDown];
}

- (void)testCreateNewOrder {
	Order * order = [Order createInContext:self.moc];
	XCTAssert(order != nil, @"[NSManagedObject create] should not return nil");
}

- (void)testPopulateDB {
	Menu * menu;
	DishCategory * cat;
	MenuComposition * compo1;
	MenuComposition * compo2;
	
	[self populateDB];
	
	// Test the number of objects
	XCTAssertEqual([Dish allObjectsInContext:self.moc].count, 9, @"There should be exactly 9 dishes in the database");
	XCTAssertEqual([DishCategory allObjectsInContext:self.moc].count, 5, @"There should be exactly 5 dish categories in the database");
	XCTAssertEqual([MenuComposition allObjectsInContext:self.moc].count, 4, @"There should be exactly 4 menu compositions in the database");
	
	// Test the menus part
	menu = [Menu retrieveWithServerId:@"unexistent_menu" inContext:self.moc];
	XCTAssertNil(menu, @"Should not retrieve a value : %@", menu);
	menu = [Menu retrieveWithServerId:@"menu" inContext:self.moc];
	XCTAssert(menu != nil, @"Should retrieve a value with serverId `menu`");
	
	XCTAssertEqual(menu.compositions.count, 2, @"Menu should have 2 compositions");
	NSArray * array = menu.compositions.allObjects;
	compo1 = array.firstObject;
	compo2 = array.lastObject;
	if (![compo1.serverId isEqualToString:@"compo1"]) {
		compo1 = array.lastObject;
		compo2 = array.firstObject;
	}
	
	XCTAssertEqualObjects(compo1.serverId, @"compo1", @"Invalid serverId for compo1: %@", compo1.serverId);
	XCTAssertEqualObjects(compo2.serverId, @"compo2", @"Invalid serverId for compo2: %@", compo2.serverId);
	XCTAssertEqual(compo1.categories.count, 2, @"Compo1 should have 2 categories");
	XCTAssertEqual(compo2.categories.count, 2, @"Compo2 should have 2 categories");
	DishCategory * category = [DishCategory retrieveWithServerId:@"cat1" inContext:self.moc];
	XCTAssertEqual(category.compositions.count, 1, @"Category %@ should be in only one composition", category);
	category = [DishCategory retrieveWithServerId:@"cat2" inContext:self.moc];
	XCTAssertEqual(category.compositions.count, 2, @"Category %@ should be in two compositions", category);
	cat = compo1.categories.allObjects.firstObject;
	XCTAssertEqual(cat.dishes.count, 2, @"Compo1 categories should have 2 dishes");
	cat = compo1.categories.allObjects.lastObject;
	XCTAssertEqual(cat.dishes.count, 2, @"Compo1 categories should have 2 dishes");
	cat = compo2.categories.allObjects.firstObject;
	XCTAssertEqual(cat.dishes.count, 2, @"Compo2 categories should have 2 dishes");
	cat = compo2.categories.allObjects.lastObject;
	XCTAssertEqual(cat.dishes.count, 2, @"Compo2 categories should have 2 dishes");
	
	
	// Test the alacarte part
	MenuModel * menuModel = [MenuModel new];
	menuModel.context = self.moc;
	menu = menuModel.alacarte;
	XCTAssertEqual(menu.compositions.count, 2, @"Alacarte should have 2 composition");
	NSArray * tmp = menu.compositions.allObjects;
	compo1 = tmp.firstObject;
	compo2 = tmp.lastObject;
	XCTAssertEqual(compo1.categories.count, 1, @"Alacarte compos should have only 1 category");
	cat = compo1.categories.allObjects.firstObject;
	XCTAssertEqual(cat.dishes.count, 2, @"Alacarte categories should have 2 dishes");
	cat = compo2.categories.allObjects.firstObject;
	XCTAssertEqual(cat.dishes.count, 2, @"Alacarte categories should have 2 dishes");
}

- (void)testOrderJSONEmpty {
	Order * order = [Order createInContext:self.moc];
	NSDictionary * json = order.toJSON;
	NSDictionary * dict = @{
							@"numTable": @"",
							@"numPA": @0,
							@"globalComment": @"",
							@"order": @[]
							};
	
	XCTAssertEqualObjects(json, dict, @"Empty order JSON is not right : `%@` != `%@`", json, dict);
}

- (void)testOrderJSONTable {
	Order * order = [Order createInContext:self.moc];
	order.table_id = @"2";
	
	
	NSDictionary * json = order.toJSON;
	NSDictionary * dict = @{
							@"numTable": @"2",
							@"numPA": @0,
							@"globalComment": @"",
							@"order": @[],
							};
	
	XCTAssertEqualObjects(json, dict, @"Table order JSON is not right : `%@` != `%@`", json, dict);
}

- (void)testorderedDishWithDishAndCompositionEmpty {
	Order * order = [Order createInContext:self.moc];
	XCTAssertNil([order orderedDishWithDish:nil andComposition:nil], @"I should not be getting any answer");
	
	// Create a dish in a composition
	Menu * menu = [Menu createInContext:self.moc];
	MenuComposition * comp = [MenuComposition createInContext:self.moc];
	comp.menu = menu;
	DishCategory * cat = [DishCategory createInContext:self.moc];
	[comp addCategoriesObject:cat];

	Dish * dish = [Dish createInContext:self.moc];
	dish.name = @"Dish 1";
	[cat addDishesObject:dish];
	
	// And test with some value
	XCTAssertNil([order orderedDishWithDish:dish andComposition:comp], @"I should not be getting any answer");
}

- (void)testorderedDishWithDishAndCompositionNil {
	[self populateDB];
	
	// Initialize variables
	Order * order = [Order createInContext:self.moc];

	XCTAssertNotNil(order);
//	XCTAssertNil([order orderedDishWithDish:dish4 andComposition:compo1], @"I should not get any answer");
//	XCTAssertNil([order orderedDishWithDish:dish6 andComposition:compo2], @"I should not get any answer");
//	XCTAssertNil([order orderedDishWithDish:dish1 andComposition:compo2], @"I should not get any answer");
}

- (void)testorderedDishWithDishAndComposition {
	[self populateDB];
	
}

@end
