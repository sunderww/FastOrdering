//
//  Plan.h
//  FastOrdering
//
//  Created by Sunder on 22/10/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Table;

@interface Plan : NSManagedObject

@property (nonatomic, retain) NSDate * createdAt;
@property (nonatomic, retain) NSString * serverId;
@property (nonatomic, retain) NSString * sizeString;
@property (nonatomic, retain) NSDate * updatedAt;
@property (nonatomic, retain) NSSet *tables;
@end

@interface Plan (CoreDataGeneratedAccessors)

- (void)addTablesObject:(Table *)value;
- (void)removeTablesObject:(Table *)value;
- (void)addTables:(NSSet *)values;
- (void)removeTables:(NSSet *)values;

@end
