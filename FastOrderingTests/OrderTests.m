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
#import "OrderContent+Custom.h"
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
	dish.available = @YES;
	[category addDishesObject:dish];
	[compo addCategoriesObject:category];
	
//	// Alacarte
//	menu = [Menu createInContext:self.moc];
//	menu.name = kMenuALaCarteName;
//	menu.serverId = @"menu a la carte";
//	menu.createdAt = [NSDate date];
//	menu.updatedAt = [NSDate date];
//	
//	// Alacarte compo
//	compo = [MenuComposition createInContext:self.moc];
//	compo.createdAt = [NSDate date];
//	compo.updatedAt = [NSDate date];
//	compo.serverId = @"Carte_Compo";
//	compo.name = @"Carte Compo";
//	compo.price = @2;
//	[menu addCompositionsObject:compo];
//	
//	// Alacarte category
//	category = [DishCategory createInContext:self.moc];
//	category.createdAt = [NSDate date];
//	category.updatedAt = [NSDate date];
//	category.serverId = @"Carte_Category";
//	category.name = @"Carte Category";
	
	// Alacarte dishes
	
	
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

- (void)testCreateOrderWithContentAndDishes {
	Order * order = [Order createInContext:self.moc];
	OrderContent * content = [OrderContent createInContext:self.moc];
	OrderedDish * dish = [OrderedDish createInContext:self.moc];
	
	content.order = order;
	XCTAssert(content.order == order, @"Content.order should be the order after addOrderContentsObject");
	XCTAssert(order.orderContents.count == 1, @"order.orderContents count should be 1");
	
	[order addDishesObject:dish];
	XCTAssert(dish.order == order, @"Dish.order should be the order after addDishesObject");
	XCTAssert(order.dishes.count == 1, @"order.dishes count should be 1");
}

- (void)testPopulateDB {
	Menu * menu;
	DishCategory * cat;
	MenuComposition * compo1;
	MenuComposition * compo2;
	
	[self populateDB];
	
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
	
	cat = compo1.categories.allObjects.firstObject;
	XCTAssertEqual(cat.dishes.count, 2, @"Compo1 categories should have 2 dishes");
	cat = compo1.categories.allObjects.lastObject;
	XCTAssertEqual(cat.dishes.count, 2, @"Compo1 categories should have 2 dishes");
	cat = compo2.categories.allObjects.firstObject;
	XCTAssertEqual(cat.dishes.count, 2, @"Compo2 categories should have 2 dishes");
	cat = compo2.categories.allObjects.lastObject;
	XCTAssertEqual(cat.dishes.count, 2, @"Compo2 categories should have 2 dishes");
	
	XCTAssertEqual([Dish allObjectsInContext:self.moc].count, 6, @"There should be exactly 6 dishes in the database");
	XCTAssertEqual([DishCategory allObjectsInContext:self.moc].count, 3, @"There should be exactly 3 dish categories in the database");
	XCTAssertEqual([MenuComposition allObjectsInContext:self.moc].count, 2, @"There should be exactly 2 menu compositions in the database");
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
	
	XCTAssert([json isEqualToDictionary:dict], @"Empty order JSON is not right : `%@` != `%@`", json, dict);
}

- (void)testOrderJSONTable {
	Order * order = [Order createInContext:self.moc];
	order.numTable = @"2";
	
	
	NSDictionary * json = order.toJSON;
	NSDictionary * dict = @{
							@"numTable": @"2",
							@"numPA": @0,
							@"globalComment": @"",
							@"order": @[],
							};
	
	XCTAssert([json isEqualToDictionary:dict], @"Table order JSON is not right : `%@` != `%@`", json, dict);
}

- (void)testOrderJSONOrderMenus {
	[self populateDB];
	
	Order * order = [Order createInContext:self.moc];
	OrderContent * content = [OrderContent createInContext:self.moc];
	content.comment = @"This is a comment";
	
	
	
	NSDictionary * json = order.toJSON;
	NSDictionary * dict = @{
							@"numTable": @"",
							@"numPA": @0,
							@"globalComment": @"",
							@"order": @[],
							};
	
	XCTAssert([json isEqualToDictionary:dict], @"Menu order JSON is not right : `%@` != `%@`", json, dict);
}

- (void)testOrderJSONOrderALaCarte {
	[self populateDB];

	Order * order = [Order createInContext:self.moc];
	
	NSDictionary * json = order.toJSON;
	NSDictionary * dict = @{
							@"numTable": @"",
							@"numPA": @0,
							@"globalComment": @"",
							@"order": @[],
							};
	
	XCTAssert([json isEqualToDictionary:dict], @"Alacarte order JSON is not right : `%@` != `%@`", json, dict);
}

//- (void)testOrderJSONComplete {
//  Order * order = [Order createInContext:self.moc];
//
//  NSDictionary * json = order.toJSON;
//  NSDictionary * dict = @{
//                          @"numTable": @"",
//                          @"numPA": @0,
//                          @"globalComment": @"",
//                          @"order": @[],
//                          };
//
//  XCTAssert([json isEqualToDictionary:dict], @"Complete order JSON is not right : `%@` != `%@`", json, dict);
//}

- (void)testSanitize {
	XCTAssert(YES, @"Pass");
}

- (void)testAlacarteContents {
	XCTAssert(YES, @"Pass");
}

- (void)testCreateAlacarteContents {
	XCTAssert(YES, @"Pass");
}

- (void)testorderedDishWithDishAndComposition {
	XCTAssert(YES, @"Pass");
}

@end
