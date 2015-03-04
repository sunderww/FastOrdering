//
//  SyncTests.m
//  FastOrdering
//
//  Created by Sunder on 02/03/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <XCTest/XCTest.h>

#define kServerPath     @"/Users/admin/Documents/Epitech/EIP/Server"
#define kServerCmd      [kServerPath stringByAppendingPathComponent:@"sails"]
#define kServerArgs     @[@"lift"]

@interface SyncTests : XCTestCase

@end

@implementation SyncTests

- (void)setUp {
    [super setUp];
    // Put setup code here. This method is called before the invocation of each test method in the class.
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];
}

- (void)testExample {
    // This is an example of a functional test case.
    XCTAssert(YES, @"Pass");
}

- (void)testPerformanceExample {
    // This is an example of a performance test case.
    [self measureBlock:^{
        // Put the code you want to measure the time of here.
    }];
}

@end
