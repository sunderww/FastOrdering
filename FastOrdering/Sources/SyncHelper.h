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
 */
- (void)syncClassNamed:(NSString *)name;

/*
 * syncDeletedObjects : is used to retrieve deleted objects from the server and delete them from coredata
 * The server need a `Deleted` table with `deletedClass` and `deletedId` fields
 * You can choose to delete objects of only few classes
 */
- (void)syncDeletedObjectsOfClasses:(NSArray *)classes;

@property (nonatomic, retain) id<SyncerDelegate>    delegate;
@property (nonatomic, copy) NSString *              restaurantId;
@property (nonatomic, copy) NSString *              waiterId;

@end
