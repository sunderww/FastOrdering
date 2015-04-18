//
//  ReviewExpandableCell.h
//  FastOrdering
//
//  Created by Sunder on 08/04/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SLExpandableTableView.h"

@interface ReviewExpandableCell : UITableViewCell <UIExpandingTableViewCell>

@property (nonatomic, retain) NSString * text;
@property (nonatomic, assign, getter = isLoading) BOOL loading;
@property (nonatomic, readonly) UIExpansionStyle expansionStyle;

- (void)setExpansionStyle:(UIExpansionStyle)expansionStyle animated:(BOOL)animated;

@end
