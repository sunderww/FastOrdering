//
//  AppDelegate.h
//  FastOrdering
//
//  Created by Sunder on 15/10/2014.
//  Copyright (c) 2014 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SocketHelper.h"

@import CoreData;

@class Notification;
@class MainViewController;


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

#define kServerKey	@"$2a$10$hNRDTYzRYtvADp0TzWN1Zur9eSkklhqpd56N1Jp7TMSTZeW/XLgY2"


#pragma mark - AppDelegate

@interface AppDelegate : UIResponder <UIApplicationDelegate, SocketEventListener> {
	NSMutableArray *	contexts;
}

@property (strong, nonatomic) UIWindow *window;

@property (readonly, strong, nonatomic) NSManagedObjectContext *managedObjectContext;
@property (readonly, strong, nonatomic) NSManagedObjectModel *managedObjectModel;
@property (readonly, strong, nonatomic) NSPersistentStoreCoordinator *persistentStoreCoordinator;
@property (strong, nonatomic) MainViewController * mainController;

@property (nonatomic, retain) NSString * restaurantId;

- (void)saveContext;
- (void)createNestedContext;
- (void)deleteNestedContext;
- (void)mergeNestedContext;
- (NSManagedObjectContext *)originalContext;

- (NSURL *)applicationDocumentsDirectory;
- (void)loadCoreData;

- (void)handleNotification:(Notification *)notification;

- (NSString *)appVersion;
- (NSString *)build;
- (NSString *)versionBuild;

@end
