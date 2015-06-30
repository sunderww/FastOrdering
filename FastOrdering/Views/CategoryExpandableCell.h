//
//  CategoryExpandableCell.h
//  FastOrdering
//
//  Created by Sunder on 23/03/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SLExpandableTableView.h"
#import "DishCategory.h"
#import "MenuComposition.h"

@interface CategoryExpandableCell : UITableViewCell <UIExpandingTableViewCell>

@property (nonatomic, retain) DishCategory * category;
@property (nonatomic, retain) MenuComposition * composition;
@property (nonatomic, assign, getter = isLoading) BOOL loading;
@property (nonatomic, readonly) UIExpansionStyle expansionStyle;

- (void)setExpansionStyle:(UIExpansionStyle)expansionStyle animated:(BOOL)animated;

@end
