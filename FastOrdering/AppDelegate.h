//
//  AppDelegate.h
//  FastOrdering
//
//  Created by Sunder on 15/10/2014.
//  Copyright (c) 2014 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>

@import CoreData;

#pragma mark - Server defines

// uncomment this line to use EIP's server
#define USE_EIP_SERVER

#ifdef USE_EIP_SERVER
//	#define kSocketIOHost		@"10.224.9.184" // IP WHEN ON EPITECH NETWORK
	#define kSocketIOHost       @"163.5.84.184" // IP THE REST OF THE TIME...
    #define kSocketIOPort       4242
#else
    #define kSocketIOHost       @"127.0.0.1"
    #define kSocketIOPort       1337
#endif
#define kSocketIOURL        [NSString stringWithFormat:@"%@:%d", kSocketIOHost, kSocketIOPort]
#define kAboutLink          @"http://fastordering.fr"

#pragma mark - Other defines
// uncomment this line to begin with an empty db
//#define kShouldDropDB

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (readonly, strong, nonatomic) NSManagedObjectContext *managedObjectContext;
@property (readonly, strong, nonatomic) NSManagedObjectModel *managedObjectModel;
@property (readonly, strong, nonatomic) NSPersistentStoreCoordinator *persistentStoreCoordinator;

- (void)saveContext;
- (NSURL *)applicationDocumentsDirectory;

- (NSString *)appVersion;
- (NSString *)build;
- (NSString *)versionBuild;

@end
