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
    XCTAssert(YES, @"Pass");
}

@end
