//
//  Notification+Custom.m
//  FastOrdering
//
//  Created by Sunder on 29/10/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "Notification+Custom.h"

@implementation Notification (Custom)

- (NSString *)computedMessage {
	NSDateFormatter * f = [NSDateFormatter new];
	f.dateFormat = @"à HH:mm";

	return [NSString stringWithFormat:@"%@, table #%@ : %@", [f stringFromDate:self.date].capitalizedString, self.numTable, self.msg];
}

@end
