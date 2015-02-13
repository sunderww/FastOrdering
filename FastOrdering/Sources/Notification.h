//
//  Notification.h
//  FastOrdering
//
//  Created by Sunder on 12/02/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface Notification : NSManagedObject

@property (nonatomic, retain) NSString * contentString;
@property (nonatomic, retain) NSDate * date;

@end
