//
//  MenuModelTest.m
//  FastOrdering
//
//  Created by Sunder on 28/06/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

@import CoreData;

#import <UIKit/UIKit.h>
#import <XCTest/XCTest.h>
#import "NSManagedObject+create.h"
#import "MenuModel.h"
#import "Menu+Custom.h"
#import "NSManagedObject+create.h"

@interface MenuModelTest : XCTestCase

@property (nonatomic, retain) NSManagedObjectContext * moc;

@end

@implementation MenuModelTest

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

- (void)testMenusWithEverything {
	NSMutableArray * expected = [NSMutableArray new];

	Menu * menu = [Menu createInContext:self.moc];
	menu.name = @"menu1";
	[expected addObject:menu];
	
	menu = [Menu createInContext:self.moc];
	menu.name = @"";
	[expected addObject:menu];
	
	menu = [Menu createInContext:self.moc];
	// try without setting a name
	[expected addObject:menu];
	
	menu = [Menu createInContext:self.moc];
	menu.name = kMenuALaCarteName;

	MenuModel * model = [MenuModel new];
	model.context = self.moc;

	NSArray * menus = model.menus;
	XCTAssertEqual(menus.count, expected.count);
	for (Menu * m in menus) {
		XCTAssertNotEqualObjects(m.name, kMenuALaCarteName, @"%@ should not be in the menus", kMenuALaCarteName);
	}
}

- (void)testALaCarteWithEverything {
	Menu * menu = [Menu createInContext:self.moc];
	menu.name = @"menu1";
	
	menu = [Menu createInContext:self.moc];
	menu.name = @"";
	
	menu = [Menu createInContext:self.moc];
	// try without setting a name

	menu = [Menu createInContext:self.moc];
	menu.name = kMenuALaCarteName;

	
	MenuModel * model = [MenuModel new];
	model.context = self.moc;
	XCTAssertEqualObjects(model.alacarte, menu, @"Alacarte menus should be equals");
	XCTAssertEqualObjects(model.alacarte.name, kMenuALaCarteName, @"alacarte name should be %@", kMenuALaCarteName);
}

- (void)testMenusEmptyMenus {
	Menu * menu = [Menu createInContext:self.moc];
	menu.name = kMenuALaCarteName;
	
	MenuModel * model = [MenuModel new];
	model.context = self.moc;
	XCTAssertEqual(model.menus.count, 0, @"There shouldn't be any menus with only alacarte in database");
}

- (void)testALaCarteEmptyMenus {
	Menu * menu = [Menu createInContext:self.moc];
	menu.name = kMenuALaCarteName;
	
	MenuModel * model = [MenuModel new];
	model.context = self.moc;
	XCTAssertEqualObjects(model.alacarte, menu, @"Alacarte menus should be equals");
	XCTAssertEqualObjects(model.alacarte.name, kMenuALaCarteName, @"alacarte name should be %@", kMenuALaCarteName);}

- (void)testMenusEmptyCarte {
	NSMutableArray * expected = [NSMutableArray new];

	Menu * menu = [Menu createInContext:self.moc];
	menu.name = @"menu1";
	[expected addObject:menu];
	
	menu = [Menu createInContext:self.moc];
	menu.name = @"";
	[expected addObject:menu];
	
	menu = [Menu createInContext:self.moc];
	// try without setting a name
	[expected addObject:menu];
	
	MenuModel * model = [MenuModel new];
	model.context = self.moc;
	XCTAssertEqual(model.menus.count, expected.count, @"There should only be %lu menus", (unsigned long)expected.count);
}

- (void)testALaCarteEmptyCarte {
	Menu * menu = [Menu createInContext:self.moc];
	menu.name = @"menu1";
	
	menu = [Menu createInContext:self.moc];
	menu.name = @"";

	menu = [Menu createInContext:self.moc];
	// try without setting a name
	
	MenuModel * model = [MenuModel new];
	model.context = self.moc;
	
	XCTAssertNil(model.alacarte, @"Expected alacarte to be nil with only menus in database");
}

- (void)testMenusEmpty {
	MenuModel * model = [MenuModel new];
	model.context = self.moc;
	
	XCTAssertEqual(model.menus.count, 0, @"There shouldn't be any menus with no objects in database");
}

- (void)testALaCarteEmpty {
	MenuModel * model = [MenuModel new];
	model.context = self.moc;

	XCTAssertNil(model.alacarte, @"Expected alacarte to be nil with no object in database");
}

@end
