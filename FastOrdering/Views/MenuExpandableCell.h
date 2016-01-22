//
//  MenuExpandableCell.h
//  FastOrdering
//
//  Created by Sunder on 21/03/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SLExpandableTableView.h"
#import "Menu.h"

@interface MenuExpandableCell : UITableViewCell <UIExpandingTableViewCell> {
	UIImageView * arrow;
}

@property (nonatomic, retain) Menu * menu;
@property (nonatomic, assign, getter = isLoading) BOOL loading;
@property (nonatomic, readonly) UIExpansionStyle expansionStyle;

- (void)setExpansionStyle:(UIExpansionStyle)expansionStyle animated:(BOOL)animated;
- (void)initialize;

@end
