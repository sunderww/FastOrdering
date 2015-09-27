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
	
	// Alacarte compo
	compo = [MenuComposition createInContext:self.moc];
	compo.createdAt = [NSDate date];
	compo.updatedAt = [NSDate date];
	compo.serverId = @"Carte_Compo";
	compo.name = @"Carte Compo";
	[menu addCompositionsObject:compo];
	
	// Alacarte category desserts
	category = [DishCategory createInContext:self.moc];
	category.createdAt = [NSDate date];
	category.updatedAt = [NSDate date];
	category.serverId = @"Carte_Category_1";
	category.name = @"Desserts";
	[compo addCategoriesObject:category];
	
	// Alacarte dishes - we add one dessert from a menu and a second dessert
	[category addDishesObject:dish];
	dish = [Dish createInContext:self.moc];
	dish.createdAt = [NSDate date];
	dish.updatedAt = [NSDate date];
	dish.serverId = @"dish7";
	dish.price = @3;
	dish.name = @"dessert 3";
	dish.available = @YES;
	[category addDishesObject:dish];
	
	// Alacarte category plats
	category = [DishCategory createInContext:self.moc];
	category.createdAt = [NSDate date];
	category.updatedAt = [NSDate date];
	category.serverId = @"Carte_Category_2";
	category.name = @"Plats";
	[compo addCategoriesObject:category];
	
	// Alacarte dishes
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
	
	// Test the number of objects
	XCTAssertEqual([Dish allObjectsInContext:self.moc].count, 9, @"There should be exactly 9 dishes in the database");
	XCTAssertEqual([DishCategory allObjectsInContext:self.moc].count, 5, @"There should be exactly 5 dish categories in the database");
	XCTAssertEqual([MenuComposition allObjectsInContext:self.moc].count, 3, @"There should be exactly 3 menu compositions in the database");
	
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
//	category = [DishCategory retrieveWithServerId:@"cat2" inContext:self.moc];
//	XCTAssertEqual(category.compositions.count, 2, @"Category %@ should be in two compositions", category);
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
	XCTAssertEqual(menu.compositions.count, 1, @"Alacarte should have 1 composition");
	compo1 = menu.compositions.allObjects.firstObject;
	XCTAssertEqual(compo1.categories.count, 2, @"Alacarte compo should have 2 categories");
	cat = compo1.categories.allObjects.firstObject;
	XCTAssertEqual(cat.dishes.count, 2, @"Alacarte categories should have 2 dishes");
	cat = compo1.categories.allObjects.lastObject;
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

- (void)testSanitizeEmpty {
	Order * order = [Order createInContext:self.moc];
	XCTAssertEqual(order.orderContents.count, 0, @"Order should not have content");
	[order sanitizeInContext:self.moc];
	XCTAssertEqual(order.orderContents.count, 0, @"Order should not have content");
}

- (void)testSanitizeWithSimpleSanitizing {
	Order * order = [Order createInContext:self.moc];
	
	// Empty OrderContent
	OrderContent * content = [OrderContent createInContext:self.moc];
	content.order = order;
	
	XCTAssertEqual(order.orderContents.count, 1, @"Order should have 1 content");
	[order sanitizeInContext:self.moc];
	XCTAssertEqual(order.orderContents.count, 0, @"Order should not have content");
}

- (void)testSanitizeWithComplexSanitizing {
	Order * order = [Order createInContext:self.moc];

	// OrderContent with one dish at @0 qt and the other one without quantity
	OrderContent * content = [OrderContent createInContext:self.moc];
	content.order = order;
	
	Dish * dish = [Dish createInContext:self.moc];
	dish.name = @"Empty dish";
	
	OrderedDish * ordered = [OrderedDish createInContext:self.moc];
	ordered.dish = dish;
	ordered.content = content;
	ordered.quantity = @0;
	
	dish = [Dish createInContext:self.moc];
	dish.name = @"No Quantity Dish";
	
	ordered = [OrderedDish createInContext:self.moc];
	ordered.dish = dish;
	ordered.content = content;
	
	// Actual tests
	XCTAssertEqual(order.orderContents.count, 1, @"Order should have 1 content");
	[order sanitizeInContext:self.moc];
	XCTAssertEqual(order.orderContents.count, 0, @"Order should not have content");
}

- (void)testSanitizeWithoutSanitizing {
	Order * order = [Order createInContext:self.moc];
	NSMutableArray * contents = [NSMutableArray new];

	// First OrderContent
	OrderContent * content = [OrderContent createInContext:self.moc];
	content.order = order;
	[contents addObject:content];
	
	// First Dish
	Dish * dish = [Dish createInContext:self.moc];
	dish.name = @"Dish 1";
	OrderedDish * ordered = [OrderedDish createInContext:self.moc];
	ordered.dish = dish;
	ordered.content = content;
	ordered.quantity = @4;
	
	// Second Dish
	dish = [Dish createInContext:self.moc];
	dish.name = @"Dish 2";
	ordered = [OrderedDish createInContext:self.moc];
	ordered.dish = dish;
	ordered.quantity = @1;
	ordered.content = content;
	
	// Second OrderContent
	content = [OrderContent createInContext:self.moc];
	content.order = order;
	[contents addObject:content];

	// Third dish
	dish = [Dish createInContext:self.moc];
	dish.name = @"Dish 3";
	ordered = [OrderedDish createInContext:self.moc];
	ordered.dish = dish;
	ordered.quantity = @2;
	ordered.content = content;

	XCTAssertEqual(order.orderContents.count, contents.count, @"Order should have %lu content", contents.count);
	[order sanitizeInContext:self.moc];
	XCTAssertEqual(order.orderContents.count, contents.count, @"Order should have %lu content", contents.count);
}

- (void)testSanitizeComplete {
	Order * order = [Order createInContext:self.moc];
	NSMutableArray * contents = [NSMutableArray new];
	NSMutableArray * emptyContents = [NSMutableArray new];
	
	// First OrderContent not empty but sanitizing
	OrderContent * content = [OrderContent createInContext:self.moc];
	content.order = order;
	[contents addObject:content];
	
	// First Dish
	Dish * dish = [Dish createInContext:self.moc];
	dish.name = @"Dish 1";
	OrderedDish * ordered = [OrderedDish createInContext:self.moc];
	ordered.dish = dish;
	ordered.content = content;
	ordered.quantity = @4;
	
	// Second Dish
	dish = [Dish createInContext:self.moc];
	dish.name = @"Dish 2";
	ordered = [OrderedDish createInContext:self.moc];
	ordered.dish = dish;
	ordered.quantity = @0;
	ordered.content = content;
	
	
	// Second OrderContent empty
	content = [OrderContent createInContext:self.moc];
	content.order = order;
	[contents addObject:content];
	[emptyContents addObject:content];
	
	// Third dish
	dish = [Dish createInContext:self.moc];
	dish.name = @"Dish 3";
	ordered = [OrderedDish createInContext:self.moc];
	ordered.dish = dish;
	ordered.quantity = @0;
	ordered.content = content;
	
	// Third OrderContent empty
	content = [OrderContent createInContext:self.moc];
	content.order = order;
	[contents addObject:content];
	[emptyContents addObject:content];
	
	
	// Fourth OrderContent not empty
	content = [OrderContent createInContext:self.moc];
	content.order = order;
	[contents addObject:content];
	
	// Fourth dish
	dish = [Dish createInContext:self.moc];
	dish.name = @"Dish 4";
	ordered = [OrderedDish createInContext:self.moc];
	ordered.dish = dish;
	ordered.quantity = @5;
	ordered.content = content;
	
	
	// Actual tests
	XCTAssertEqual(order.orderContents.count, contents.count, @"Order should have %lu contents", contents.count);
	[order sanitizeInContext:self.moc];
	XCTAssertEqual(order.orderContents.count, contents.count - emptyContents.count, @"Order should have %lu contents", contents.count - emptyContents.count);
	[order sanitizeInContext:self.moc];
	XCTAssertEqual(order.orderContents.count, contents.count - emptyContents.count, @"Order should have %lu contents", contents.count - emptyContents.count);
}


- (void)testAlacarteContents {
	XCTAssert(YES, @"Pass");
}

- (void)testCreateAlacarteContents {
	XCTAssert(YES, @"Pass");
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
	OrderContent * content = [OrderContent createInContext:self.moc];
	MenuComposition * compo2 = [MenuComposition retrieveWithServerId:@"compo2" inContext:self.moc];
	MenuComposition * compo1 = [MenuComposition retrieveWithServerId:@"compo1" inContext:self.moc];
	Dish * dish1 = [Dish retrieveWithServerId:@"dish1" inContext:self.moc]; // Entrée 1
	Dish * dish4 = [Dish retrieveWithServerId:@"dish4" inContext:self.moc]; // Plat 2
	Dish * dish5 = [Dish retrieveWithServerId:@"dish5" inContext:self.moc]; // Dessert 1
	Dish * dish6 = [Dish retrieveWithServerId:@"dish6" inContext:self.moc]; // Dessert 2
	content.order = order;
	content.menuComposition = compo2;

	// Be careful to put dishes that are inside the menucomposition
	OrderedDish * ordered = [OrderedDish createInContext:self.moc];
	ordered.dish = dish4;
	ordered.quantity = @1;
	ordered.content = content;
	
	ordered = [OrderedDish createInContext:self.moc];
	ordered.dish = dish5;
	ordered.quantity = @1;
	ordered.content = content;

	
	XCTAssertNil([order orderedDishWithDish:dish4 andComposition:compo1], @"I should not get any answer");
	XCTAssertNil([order orderedDishWithDish:dish6 andComposition:compo2], @"I should not get any answer");
	XCTAssertNil([order orderedDishWithDish:dish1 andComposition:compo2], @"I should not get any answer");
}

- (void)testorderedDishWithDishAndComposition {
	[self populateDB];
	
	// Initialize variables
	Order * order = [Order createInContext:self.moc];
	OrderContent * content1 = [OrderContent createInContext:self.moc];
	OrderContent * content2 = [OrderContent createInContext:self.moc];
	MenuComposition * compo2 = [MenuComposition retrieveWithServerId:@"compo2" inContext:self.moc];
	MenuComposition * carte = [MenuComposition retrieveWithServerId:@"Carte_Compo" inContext:self.moc];
	Dish * dish4 = [Dish retrieveWithServerId:@"dish4" inContext:self.moc]; // Plat 2
	Dish * dish6 = [Dish retrieveWithServerId:@"dish6" inContext:self.moc]; // Dessert 2
	Dish * dish9 = [Dish retrieveWithServerId:@"dish9" inContext:self.moc]; // Plat carte 2
	content1.order = order;
	content1.menuComposition = compo2;
	content2.order = order;
	content2.menuComposition = carte;
	
	// Be careful to put dishes that are inside the correct menucomposition
	OrderedDish * ordered4 = [OrderedDish createInContext:self.moc];
	ordered4.dish = dish4;
	ordered4.quantity = @1;
	ordered4.content = content1;
	
	OrderedDish * ordered6Menu = [OrderedDish createInContext:self.moc];
	ordered6Menu.dish = dish6;
	ordered6Menu.quantity = @1;
	ordered6Menu.content = content1;
	
	XCTAssertNil([order orderedDishWithDish:dish6 andComposition:carte], @"I should not get dish6 with carte composition yet");
	OrderedDish * ordered6Carte = [OrderedDish createInContext:self.moc];
	ordered6Carte.dish = dish6;
	ordered6Carte.quantity = @1;
	ordered6Carte.content = content2;
	
	OrderedDish * ordered9 = [OrderedDish createInContext:self.moc];
	ordered9.dish = dish9;
	ordered9.quantity = @1;
	ordered9.content = content2;
	
	XCTAssertEqual([order orderedDishWithDish:dish4 andComposition:compo2], ordered4, @"orderedDishWithDish:andComposition: did not returned the expected object");
	XCTAssertEqual([order orderedDishWithDish:dish6 andComposition:compo2], ordered6Menu, @"orderedDishWithDish:andComposition: did not returned the expected object");
	XCTAssertEqual([order orderedDishWithDish:dish6 andComposition:carte], ordered6Carte, @"orderedDishWithDish:andComposition: did not returned the expected object");
	XCTAssertEqual([order orderedDishWithDish:dish9 andComposition:carte], ordered9, @"orderedDishWithDish:andComposition: did not returned the expected object");
}

@end
