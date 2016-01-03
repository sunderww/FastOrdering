//
//  AppDelegate.m
//  FastOrdering
//
//  Created by Sunder on 15/10/2014.
//  Copyright (c) 2014 lucas.bergognon. All rights reserved.
//

#import "AppDelegate.h"
#import "LoginViewController.h"
#import "Notification+Custom.h"
#import "NSManagedObject+create.h"
#import "MainViewController.h"

@implementation AppDelegate

@synthesize managedObjectContext = _managedObjectContext;
@synthesize managedObjectModel = _managedObjectModel;
@synthesize persistentStoreCoordinator = _persistentStoreCoordinator;

#pragma mark - Application life cycle

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
	contexts = [NSMutableArray new];
	LoginViewController * controller;

	// Delete all old notifications
//	DLog(@"Deleting all notifications...");
//	[self deleteAllObjects:@"Notification"];
	
	// Register to notifications
	[[SocketHelper sharedHelper] registerListener:self forEvent:@"notifications"];
	if ([application respondsToSelector:@selector(registerUserNotificationSettings:)]) {
		[application registerUserNotificationSettings:[UIUserNotificationSettings settingsForTypes:UIUserNotificationTypeAlert|UIUserNotificationTypeSound categories:nil]];
	}
	
	// Handle the open-by-a-notification case
	UILocalNotification *notification = [launchOptions objectForKey:UIApplicationLaunchOptionsLocalNotificationKey];
	if (notification) {
		[self application:[UIApplication sharedApplication] didReceiveLocalNotification:notification];
	}

	self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
	// Override point for customization after application launch.
	self.window.backgroundColor = [UIColor whiteColor];
	
	controller = [[LoginViewController alloc] initWithNibName:@"LoginView" bundle:nil];
	self.window.rootViewController = [[UINavigationController alloc] initWithRootViewController:controller];
	((UINavigationController *)self.window.rootViewController).navigationBarHidden = YES;
	
	[self.window makeKeyAndVisible];
	
	return YES;
	
}

- (void)applicationWillResignActive:(UIApplication *)application
{
	// Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
	// Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
	// Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
	// If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
	// Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
	// Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
	// Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

#pragma mark - Socket methods

- (void)connectToSocket {
	
}

- (void)saveContext
{
	NSError *error = nil;
	NSManagedObjectContext *managedObjectContext = self.originalContext;
	if (managedObjectContext != nil) {
		if ([managedObjectContext hasChanges] && ![managedObjectContext save:&error]) {
			// Replace this implementation with code to handle the error appropriately.
			// abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
			NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
			abort();
		}
	}
}

#pragma mark - Core Data related methods

#if DEBUG

- (void)deleteAllObjects:(NSString *)entityDescription  {
	NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
	NSEntityDescription *entity = [NSEntityDescription entityForName:entityDescription inManagedObjectContext:self.managedObjectContext];
	[fetchRequest setEntity:entity];
	
	NSError *error;
	NSArray *items = [_managedObjectContext executeFetchRequest:fetchRequest error:&error];
	
	for (NSManagedObject *managedObject in items) {
		[_managedObjectContext deleteObject:managedObject];
		PPLog(@"%@ object deleted", entityDescription);
	}
	if (![_managedObjectContext save:&error]) {
		PPLog(@"Error deleting %@ - error:%@", entityDescription, error);
	}
}

- (void)tryDropDB {
	// This function drops the db if we change the server source (from local to remote and the other way)
	NSUserDefaults * defaults = NSUserDefaults.standardUserDefaults;
	
	if ([defaults boolForKey:@"EIPServer"]) {
#ifndef USE_EIP_SERVER
		[self dropDB];
#endif
	} else {
#ifdef USE_EIP_SERVER
		[self dropDB];
#endif
	}
	
#ifdef USE_EIP_SERVER
	[defaults setBool:@YES forKey:@"EIPServer"];
#else
	[defaults setBool:@NO forKey:@"EIPServer"];
#endif
	[defaults synchronize];
}

- (void)dropDB {
	PPLog(@"========= DELETING DB");
	for (NSString * key in self.managedObjectModel.entitiesByName)
		[self deleteAllObjects:key];
}

- (void)printDB {
	PPLog(@"========= DB DUMP");
	
	for (NSString * key in self.managedObjectModel.entitiesByName) {
		NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
		NSEntityDescription *entity = [NSEntityDescription entityForName:key inManagedObjectContext:self.managedObjectContext];
		[fetchRequest setEntity:entity];
		
		NSError *error;
		NSArray *items = [self.managedObjectContext executeFetchRequest:fetchRequest error:&error];
		
		PPLog(@"%@ > %@", key, items);
	}
	
	PPLog(@"=========== END");
}

- (void)loadCoreData {
	_managedObjectContext = nil;
	_persistentStoreCoordinator = nil;
	
	[self managedObjectContext];
	
#if DEBUG
	[self printDB];
	[self tryDropDB];
#ifdef kShouldDropDB
	[self dropDB];
#endif
#endif
}

#endif

#pragma mark - Core Data stack

// Returns the managed object context for the application.
// If the context doesn't already exist, it is created and bound to the persistent store coordinator for the application.
- (NSManagedObjectContext *)managedObjectContext {
	return contexts.count > 0 ? [contexts lastObject] : self.originalContext;
}

- (NSManagedObjectContext *)originalContext {
	if (_managedObjectContext != nil) {
		return _managedObjectContext;
	}
	
	NSPersistentStoreCoordinator *coordinator = [self persistentStoreCoordinator];
	if (coordinator != nil) {
		_managedObjectContext = [[NSManagedObjectContext alloc] initWithConcurrencyType:NSMainQueueConcurrencyType];
		[_managedObjectContext setPersistentStoreCoordinator:coordinator];
		_managedObjectContext.undoManager = [NSUndoManager new];
	}
	return _managedObjectContext;
}

- (void)createNestedContext {
	NSManagedObjectContext * context = [[NSManagedObjectContext alloc] initWithConcurrencyType:NSPrivateQueueConcurrencyType];
	context.parentContext = self.managedObjectContext;
	[contexts addObject:context];
}

- (void)deleteNestedContext {
	[contexts removeLastObject];
}

- (void)mergeNestedContext {
	NSManagedObjectContext * context = [contexts lastObject];

	[context performBlockAndWait:^{
		NSError * error;
		
		if (![context save:&error]) {
			PPLog(@"Can't merge nested context : %@", error);
		} else {
			DLog(@"Merged nested context");
			dispatch_async(dispatch_get_main_queue(), ^{
				[self deleteNestedContext];
			});
		}
	}];

}

// Returns the managed object model for the application.
// If the model doesn't already exist, it is created from the application's model.
- (NSManagedObjectModel *)managedObjectModel
{
	if (_managedObjectModel != nil) {
		return _managedObjectModel;
	}
	NSURL *modelURL = [[NSBundle mainBundle] URLForResource:@"FastOrdering" withExtension:@"momd"];
	_managedObjectModel = [[NSManagedObjectModel alloc] initWithContentsOfURL:modelURL];
	return _managedObjectModel;
}

// Returns the persistent store coordinator for the application.
// If the coordinator doesn't already exist, it is created and the application's store added to it.
- (NSPersistentStoreCoordinator *)persistentStoreCoordinator
{
	if (_persistentStoreCoordinator != nil) {
		return _persistentStoreCoordinator;
	}

	NSString * dbName = [NSString stringWithFormat:@"FastOrdering-%@.sqlite", self.restaurantId];
	NSURL *storeURL = [[self applicationDocumentsDirectory] URLByAppendingPathComponent:dbName];
	
	NSError *error = nil;
	_persistentStoreCoordinator = [[NSPersistentStoreCoordinator alloc] initWithManagedObjectModel:[self managedObjectModel]];
	if (![_persistentStoreCoordinator addPersistentStoreWithType:NSSQLiteStoreType configuration:nil URL:storeURL options:nil error:&error]) {
		/*
		 Replace this implementation with code to handle the error appropriately.
		 
		 abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
		 
		 Typical reasons for an error here include:
		 * The persistent store is not accessible;
		 * The schema for the persistent store is incompatible with current managed object model.
		 Check the error message to determine what the actual problem was.
		 
		 
		 If the persistent store is not accessible, there is typically something wrong with the file path. Often, a file URL is pointing into the application's resources directory instead of a writeable directory.
		 
		 If you encounter schema incompatibility errors during development, you can reduce their frequency by:
		 * Simply deleting the existing store:
		 //*/
		[[NSFileManager defaultManager] removeItemAtURL:storeURL error:nil];
		NSLog(@"removing store");
		_persistentStoreCoordinator = nil;
		return self.persistentStoreCoordinator;
		/*
		 * Performing automatic lightweight migration by passing the following dictionary as the options parameter:
		 @{NSMigratePersistentStoresAutomaticallyOption:@YES, NSInferMappingModelAutomaticallyOption:@YES}
		 
		 Lightweight migration will only work for a limited set of schema changes; consult "Core Data Model Versioning and Data Migration Programming Guide" for details.
		 
		 */
		NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
		abort();
	}
	
	return _persistentStoreCoordinator;
}

#pragma mark - Application's Documents directory

// Returns the URL to the application's Documents directory.
- (NSURL *)applicationDocumentsDirectory
{
	return [[[NSFileManager defaultManager] URLsForDirectory:NSDocumentDirectory inDomains:NSUserDomainMask] lastObject];
}

#pragma mark - SocketEventListener methods

- (void)socketReceivedEvent:(NSString *)name withPacket:(SocketIOPacket *)packet {
	if (![name isEqualToString:@"notifications"]) return ;
	
	Notification * notification = [Notification createInContext:self.originalContext];
	NSData * data = [packet.data dataUsingEncoding:NSUTF8StringEncoding];
	NSArray * info = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:nil];
	if (info.count < 2) {
		PPLog(@"Received notifications event without correct parameters : %@", info);
		return ;
	}
	
	NSDateFormatter * formatter = [NSDateFormatter new];
	formatter.dateFormat = @"dd/MM/yy HH:mm";

	NSDictionary * notificationDict = info[1];
	NSString * date = [NSString stringWithFormat:@"%@ %@", notificationDict[@"date"], notificationDict[@"hour"]];
	notification.date = [formatter dateFromString:date];
	notification.updatedAt = [NSDate date];
	notification.msg = notificationDict[@"msg"];
	notification.numTable = notificationDict[@"numTable"];
	
	[self saveContext];
	[self handleNotification:notification];
}

#pragma mark - Notification handling

- (void)application:(UIApplication *)application didReceiveLocalNotification:(UILocalNotification *)notification {
	NSURL * url = [NSURL URLWithString:notification.userInfo[@"notificationId"]];
	NSManagedObjectID * objectID = [self.persistentStoreCoordinator managedObjectIDForURIRepresentation:url];
	Notification * notif = (Notification *)[self.originalContext existingObjectWithID:objectID error:NULL];

	
	if (self.mainController.onMainPage) {
		[self.mainController reloadData];
	} else {
		[[[UIAlertView alloc] initWithTitle:@"Notification" message:notif.msg delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil] show];
		[self.mainController reloadData];
	}
}

- (void)handleNotification:(Notification *)notification {
	UILocalNotification * local = [UILocalNotification new];

	local.alertBody = notification.computedMessage;
//	local.soundName = UILocalNotificationDefaultSoundName;
	local.userInfo = @{ @"notificationId": notification.objectID.URIRepresentation.absoluteString };
	
	[[UIApplication sharedApplication] scheduleLocalNotification:local];
}

#pragma mark - Other methods

- (NSString *)appVersion {
	return [[NSBundle mainBundle] objectForInfoDictionaryKey:@"CFBundleShortVersionString"];
}

- (NSString *)build {
	return ([[NSBundle mainBundle] objectForInfoDictionaryKey:(NSString *)kCFBundleVersionKey]);
}

- (NSString *)versionBuild {
	NSString * version = [self appVersion];
	NSString * build = [self build];
	NSString * versionBuild = [NSString stringWithFormat: @"v%@", version];
	
	if (![version isEqualToString: build])
		versionBuild = [NSString stringWithFormat: @"%@R%@", versionBuild, build];
	return versionBuild;
}

@end
