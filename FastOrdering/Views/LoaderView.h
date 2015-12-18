//
//  LoaderView.h
//  Quizz
//
//  Created by Sunder on 09/12/13.
//  Copyright (c) 2013 Sunder. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LoaderView : UIView {
    IBOutlet UIView *       view;
    IBOutlet UIView *       centerView;
    IBOutlet UILabel *      textLabel;
    BOOL                    animated;
}

@property (strong, nonatomic) NSString * title;
@property (nonatomic, assign) BOOL moved;

- (void)animate:(BOOL)flag;
- (void)reinit;

@end
