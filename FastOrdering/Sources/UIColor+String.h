//
//  UIColor+String.h
//  FastOrdering
//
//  Created by Sunder on 29/12/2014.
//  Copyright (c) 2014 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIColor (String)

+ (instancetype)colorWithString:(NSString *)string;
- (NSString *)toString;

@end
