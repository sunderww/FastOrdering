//
//  OrderedDishTest.m
//  FastOrdering
//
//  Created by Sunder on 08/10/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

@import CoreData;

#import <UIKit/UIKit.h>
#import <XCTest/XCTest.h>
#import "NSManagedObject+create.h"
#import "OrderedDish+Custom.h"
#import "Option.h"
#import "OptionCategory.h"
#import "OrderedOption.h"

@interface OrderedDishTest : XCTestCase

@property (nonatomic, retain) NSManagedObjectContext * moc;

@end

@implementation OrderedDishTest

- (void)setUp {
	[super setUp];
	
	NSURL *modelURL = [[NSBundle mainBundle] URLForResource:@"FastOrdering" withExtension:@"momd"];
	NSManagedObjectModel *mom = [[NSManagedObjectModel alloc] initWithContentsOfURL:modelURL];
	NSPersistentStoreCoordinator *psc = [[NSPersistentStoreCoordinator alloc] initWithManagedObjectModel:mom];
	XCTAssertTrue([psc addPersistentStoreWithType:NSInMemoryStoreType configuration:nil URL:nil options:nil error:NULL] ? YES : NO, @"Should be able to add in-memory store");
	self.moc = [[NSManagedObjectContext alloc] init];
	self.moc.persistentStoreCoordinator = psc;
	
	// Create db
	OptionCategory * category1 = [OptionCategory createInContext:self.moc];
	category1.serverId = @"category1";
	category1.name = @"Cuisson";
	
	Option * option = [Option createInContext:self.moc];
	option.name = @"Bleu";
	option.serverId = @"option1_bleu";
	[category1 addOptionObject:option];
	
	option = [Option createInContext:self.moc];
	option.name = @"Saignant";
	option.serverId = @"option1_saignant";
	[category1 addOptionObject:option];
	
	option = [Option createInContext:self.moc];
	option.name = @"Mal cuit";
	option.serverId = @"option1_other";
	[category1 addOptionObject:option];
	
	OptionCategory * category2 = [OptionCategory createInContext:self.moc];
	category2.serverId = @"category2";
	category2.name = @"Taille";
	
	option = [Option createInContext:self.moc];
	option.name = @"33cl";
	option.serverId = @"option2_33";
	[category1 addOptionObject:option];
	
	option = [Option createInContext:self.moc];
	option.name = @"50cl";
	option.serverId = @"option2_50";
	[category1 addOptionObject:option];
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];
}

- (void)testSanitizeEmpty {
	OrderedDish * dish = [OrderedDish createInContext:self.moc];
	XCTAssertEqual(dish.options.count, 0, @"OrderedDish should not have OrderedOptions");
	[dish sanitizeInContext:self.moc]; // Test essentially that it doesn't crash
	XCTAssertEqual(dish.options.count, 0, @"OrderedDish should not have OrderedOptions");
}

- (void)testSanitizeWithSanitizing {
	OrderedDish * dish = [OrderedDish createInContext:self.moc];

	OrderedOption * orderedOption = [OrderedOption createInContext:self.moc];
	orderedOption.option = [Option retrieveWithServerId:@"option2_50" inContext:self.moc];
	[dish addOptionsObject:orderedOption];
	
	orderedOption = [OrderedOption createInContext:self.moc];
	orderedOption.option = [Option retrieveWithServerId:@"option2_33" inContext:self.moc];
	orderedOption.qty = @0;
	[dish addOptionsObject:orderedOption];
	
	XCTAssertEqual(dish.options.count, 2, @"OrderedDish should have 2 options");
	[dish sanitizeInContext:self.moc];
	XCTAssertEqual(dish.options.count, 0, @"OrderedDish should not have options");
}

- (void)testSanitizeWithoutSanitizing {
	OrderedDish * dish = [OrderedDish createInContext:self.moc];
	
	OrderedOption * orderedOption = [OrderedOption createInContext:self.moc];
	orderedOption.option = [Option retrieveWithServerId:@"option1_bleu" inContext:self.moc];
	orderedOption.qty = @2;
	[dish addOptionsObject:orderedOption];
	
	XCTAssertEqual(dish.options.count, 1, @"OrderedDish should have 1 option");
	[dish sanitizeInContext:self.moc];
	XCTAssertEqual(dish.options.count, 1, @"OrderedDish should still have 1 option");
}

- (void)testSanitizeComplete {
	OrderedDish * dish = [OrderedDish createInContext:self.moc];
	
	OrderedOption * orderedOption = [OrderedOption createInContext:self.moc];
	orderedOption.option = [Option retrieveWithServerId:@"option1_bleu" inContext:self.moc];
	orderedOption.qty = @1;
	[dish addOptionsObject:orderedOption];
	
	orderedOption = [OrderedOption createInContext:self.moc];
	orderedOption.option = [Option retrieveWithServerId:@"option1_saignant" inContext:self.moc];
	orderedOption.qty = @2;
	[dish addOptionsObject:orderedOption];
	
	orderedOption = [OrderedOption createInContext:self.moc];
	orderedOption.option = [Option retrieveWithServerId:@"option1_other" inContext:self.moc];
	orderedOption.qty = @0;
	[dish addOptionsObject:orderedOption];
	
	XCTAssertEqual(dish.options.count, 3, @"OrderedDish should have 3 options");
	[dish sanitizeInContext:self.moc];
	XCTAssertEqual(dish.options.count, 2, @"OrderedDish should have 2 options");
}

- (void)testOrderedOptionWithOptionEmpty {
	OrderedDish * dish = [OrderedDish createInContext:self.moc];
	Option * option = [Option retrieveWithServerId:@"option2_33" inContext:self.moc];

	XCTAssertNil([dish orderedOptionWithOption:option], @"OrderedDish orderedOptionWithOption should be returning nil");
}

- (void)testOrderedOptionWithOption {
	OrderedDish * dish = [OrderedDish createInContext:self.moc];
	OrderedOption * orderedOption;
	Option * option = [Option retrieveWithServerId:@"option2_33" inContext:self.moc];
	
	orderedOption = [OrderedOption createInContext:self.moc];
	orderedOption.option = [Option retrieveWithServerId:@"option1_bleu" inContext:self.moc];
	orderedOption.qty = @1;
	[dish addOptionsObject:orderedOption];
	
	orderedOption = [OrderedOption createInContext:self.moc];
	orderedOption.option = [Option retrieveWithServerId:@"option1_saignant" inContext:self.moc];
	orderedOption.qty = @2;
	[dish addOptionsObject:orderedOption];
	
	XCTAssertNil([dish orderedOptionWithOption:option], @"OrderedDish orderedOptionWithOption should be returning nil");
}

- (void)testOrderedOptionWithOptionComplete {
	OrderedDish * dish = [OrderedDish createInContext:self.moc];
	OrderedOption * orderedOption;
	Option * option;
	
	option = [Option retrieveWithServerId:@"option1_bleu" inContext:self.moc];
	orderedOption = [OrderedOption createInContext:self.moc];
	orderedOption.option = option;
	orderedOption.qty = @1;
	[dish addOptionsObject:orderedOption];
	XCTAssertEqualObjects([dish orderedOptionWithOption:option], orderedOption, @"OrderedDish orderedOptionWithOption not returning the correct value");
	
	orderedOption = [OrderedOption createInContext:self.moc];
	orderedOption.option = [Option retrieveWithServerId:@"option1_saignant" inContext:self.moc];
	orderedOption.qty = @2;
	[dish addOptionsObject:orderedOption];

	option = [Option retrieveWithServerId:@"option1_other" inContext:self.moc];
	orderedOption = [OrderedOption createInContext:self.moc];
	orderedOption.option = option;
	orderedOption.qty = @0;
	[dish addOptionsObject:orderedOption];
	XCTAssertEqualObjects([dish orderedOptionWithOption:option], orderedOption, @"OrderedDish orderedOptionWithOption not returning the correct value");
}

@end