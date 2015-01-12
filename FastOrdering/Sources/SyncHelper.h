//
//  SyncHelper.h
//  FastOrdering
//
//  Created by Sunder on 02/01/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol SyncerDelegate <NSObject>

@optional

- (void)syncDidStartForClass:(NSString *)className;
- (void)syncDidStopForClass:(NSString *)className;
- (void)syncDidFailWithError:(NSError *)error forClass:(NSString *)className;

@end

@interface SyncHelper : NSObject {
    BOOL                hadError;
}

/*
 * syncClassNamed: is used to retrieve all objects from the server updated since last sync
 * is by default asynchronous
 */
- (void)syncClassNamed:(NSString *)name;
- (void)syncClassNamed:(NSString *)name synchronous:(BOOL)synchronous;

/*
 * syncDeletedObjects : is used to retrieve deleted objects from the server and delete them from coredata
 * The server need a `Deleted` table with `deletedClass` and `deletedId` fields
 * You can choose to delete objects of only few classes
 * is by default asynchrounous
 */
- (void)syncDeletedObjectsOfClasses:(NSArray *)classes;
- (void)syncDeletedObjectsOfClasses:(NSArray *)classes synchronous:(BOOL)synchronous;

@property (nonatomic, retain) id<SyncerDelegate>    delegate;
@property (nonatomic, copy) NSString *              restaurantId;
@property (nonatomic, copy) NSString *              waiterId;

@end
