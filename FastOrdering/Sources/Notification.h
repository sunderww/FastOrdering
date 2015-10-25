//
//  Notification.h
//  FastOrdering
//
//  Created by Sunder on 25/10/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface Notification : NSManagedObject

@property (nonatomic, retain) NSString * msg;
@property (nonatomic, retain) NSDate * date;
@property (nonatomic, retain) NSDate * time;
@property (nonatomic, retain) NSDate * updatedAt;
@property (nonatomic, retain) NSString * numTable;

@end
