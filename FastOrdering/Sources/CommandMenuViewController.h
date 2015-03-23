//
//  CommandMenuViewController.h
//  FastOrdering
//
//  Created by Sunder on 23/03/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SLExpandableTableView.h"
#import "MenuComposition.h"

@interface CommandMenuViewController : UIViewController <SLExpandableTableViewDatasource, SLExpandableTableViewDelegate, UITextFieldDelegate> {
  IBOutlet SLExpandableTableView *  expandableTableView;
  
  NSArray * categories;
  NSArray * dishes;
}

@property (nonatomic, retain) MenuComposition * composition;

@end
