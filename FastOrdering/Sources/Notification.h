//
//  Notification.h
//  FastOrdering
//
//  Created by Sunder on 10/10/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface Notification : NSManagedObject

@property (nonatomic, retain) NSString * contentString;
@property (nonatomic, retain) NSDate * date;
@property (nonatomic, retain) NSDate * updatedAt;

@end
