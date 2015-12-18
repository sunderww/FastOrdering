//
//  Table.h
//  FastOrdering
//
//  Created by Sunder on 12/11/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Plan;

@interface Table : NSManagedObject

@property (nonatomic, retain) NSDate * createdAt;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSString * serverId;
@property (nonatomic, retain) NSDate * updatedAt;
@property (nonatomic, retain) Plan *plan;

@end
